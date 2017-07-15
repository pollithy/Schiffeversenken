package logic;

import java.io.IOException;
import java.net.UnknownHostException;

import connection.*;
import graphics.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import main.Main;

public class Schiffeversenken {
	
	private EigeneMap eigeneMap;
	private GegnerischeMap gegnMap;
	private Controller contr;
	private Peer peer;
	private boolean amZug;
	
	public Schiffeversenken(Controller contr) {
		this.contr = contr;
		contr.setSchiffeversenken(this);
		peer = null;
		
		contr.zeigeIPAdresse(Peer.getIP());	// IP-Adresse wird unten in GUI angezeigt
		contr.askForPeer();		// Benutzer wird gefragt, ob er neues Spiel eröffnen will oder Spiel beitreten will
								// Je nach Eingabe wird ein Server- oder Client-Peer erzeugt
		eigeneMap = new EigeneMap();
		eigeneMap.schiffeSetzenAuto();
		gegnMap = new GegnerischeMap();
		eigeneMap.paint(contr);	// Maps werden gezeichnet
		gegnMap.paint(contr);
	}
	
	public void verbindungHergestellt() {
		Task<Void> task = new Task<Void>() {
			  @Override
			  public Void call() throws Exception {
				  Platform.runLater(new Runnable() {
		                 @Override public void run() {
		                	contr.zeigeVerbindungsstatus("Verbunden");
		             		contr.zeigeSpieldauer();
		             		contr.buttonsSchalten(amZug);
		            		if(amZug) {
		            			zugAusführen();
		            		} else {
		            			warten();
		            		}
		                 }
		             });
				return null;
			  }
			};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	private void aufVerbindungWarten() {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				contr.zeigeVerbindungsstatus("Wird hergestellt...");
			}
		});
	}
	
	public void verbindungHerstellen(String ipString) {
		try {
			aufVerbindungWarten();
			if(ipString == null) {
				amZug = true;
				peer = new Server(this);
				Thread thread = new Thread() {
					@Override
					public void run() {
						((Server) peer).connect();
					}
				};
				thread.start();
			} else {
				amZug = false;
				peer = new Client(this, ipString);
				Thread thread = new Thread() {
					@Override
					public void run() {
						((Client) peer).connect();
					}
				};
				thread.start();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void zugAusführen() {
		amZug = true;
		contr.buttonsSchalten(amZug);
		contr.setZugstatus("Sie sind am Zug");
	}
	
	public void beschiessen(int x, int y) {
		if(!eigeneMap.bereitsBeschossen(x, y)) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() throws Exception {
					try {
						peer.sendReq(new RequestProtocol(x,y));
					} catch (IOException e) {
						// Verbindung unterbrochen, Hinweis auf GUI, erneuter Verbindungsaufbau
						e.printStackTrace();
					}
					ResponseProtocol res = peer.readRes();
					if(res.isGetroffen()) {
						gegnMap.eintragen(x, y, true);	// Gegnerische Map Matrix aktualisieren
						gegnMap.paint(contr);			// Gegnerische Map grafisch aktualisieren
						Platform.runLater(new Runnable() {
							@Override 
							public void run() {
								if(gegnMap.alleVersenkt()) {
									contr.spielendeAlert(true);
									// Spiel zurücksetzen
									// evtl. neuer Verbindungsaufbau
								} else {
									zugAusführen();
								}
							}
						});					// GUI anzeigen, dass nochmal geschossen werden darf
					} else {
						gegnMap.eintragen(x, y, false);	// gegnMap aktualisieren (Niete)
						gegnMap.paint(contr);			// Gegnerische Map grafisch aktualisieren
						Platform.runLater(new Runnable() {
							@Override 
							public void run() {
								if(gegnMap.alleVersenkt()) {
									contr.spielendeAlert(true);
									// Spiel zurücksetzen
									// evtl. neuer Verbindungsaufbau
								} else {
									warten();
								}
							}
						});
					}
					return null;
				}
			};
			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	public void warten() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				amZug = false;
				Platform.runLater(new Runnable() {
					@Override 
					public void run() {
						contr.buttonsSchalten(amZug);
						contr.setZugstatus("Der Gegner ist am Zug");
						}
		            });
					RequestProtocol req = peer.readReq();
					boolean istGetroffen = eigeneMap.istGetroffen(req.getX(), req.getY());
					try {
						peer.sendRes(new ResponseProtocol(istGetroffen));
					} catch (IOException e) {
						// Verbindung unterbrochen, Hinweis auf GUI, erneuter Verbindungsaufbau
					}
					if(istGetroffen) {
						eigeneMap.paint(contr);
						Platform.runLater(new Runnable() {
							@Override 
							public void run() {
								if(eigeneMap.alleVersenkt()) {
									contr.spielendeAlert(false);
									// Spiel zurücksetzen
									// evtl. neuer Verbindungsaufbau
			                	 } else {
									 warten();
			                	 }
			                }
			            });
						// GUI anzeigen, dass nochmal gewartet werden muss bzw. der Gegner am Zug ist
					} else {
						amZug = true;
						eigeneMap.paint(contr);
						Platform.runLater(new Runnable() {
							@Override 
							public void run() {
			                	 if(eigeneMap.alleVersenkt()) {
									contr.spielendeAlert(false);
									// Spiel zurücksetzen
									// evtl. neuer Verbindungsaufbau
			                	 } else {
									 zugAusführen();
			                	 }
			                }
			            });
					}
					return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	public boolean proofIP(String ipString) {
		return Peer.proofIP(ipString);
	}
	
	public void verbindungUnterbrochen() {
		//GUI "einfrieren" lassen
	}

	public void neuesSpiel() {
		peer.disconnect();
		peer.connect();
		eigeneMap = new EigeneMap();
		eigeneMap.schiffeSetzenAuto();
		gegnMap = new GegnerischeMap();
		eigeneMap.paint(contr);	// Maps werden gezeichnet
		gegnMap.paint(contr);
		contr.askForPeer();
	}
}
