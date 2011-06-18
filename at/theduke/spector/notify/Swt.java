/**
 * 
 */
package at.theduke.spector.notify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.TrayItem;

/**
 * @author theduke
 *
 */
public class Swt {
	Application application;
	
	Display display;
	Shell shell;
	TrayItem trayItem;
	
	public void run(Application app) {
		this.application = app;
		
		display = new Display();
		shell = new Shell();
		
		initTray();
		
		// initialize database connection immediately after loop starts
		display.timerExec(500, new Runnable() {
			@Override
			public void run() {
				application.initializeMongo(application.getConfig());
			}
		});
		
		// set up the timer
		Runnable timer = new Runnable() {
			
			@Override
			public void run() {
				application.doNotify();
				
				display.timerExec(60000, this);
			}
		};
		
		display.timerExec(10000, timer);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		trayItem.dispose();
		shell.dispose();
	}
	
	protected void initTray() {
		trayItem = new TrayItem(display.getSystemTray(), SWT.NONE);
		trayItem.setImage(new Image(display, Swt.class.getResourceAsStream("trayicon.png")));
		
		// set up menu
		final Menu menu = new Menu(shell, SWT.POP_UP);
		
		final MenuItem toggleItem = new MenuItem(menu, SWT.PUSH);
		toggleItem.setText("Turn notifications OFF");
		toggleItem.setData("on");
		toggleItem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (toggleItem.getData().equals("on")) {
					application.setNotificationsEnabled(false);
					toggleItem.setData("off");
					toggleItem.setText("Turn notifications ON");
				} else {
					application.setNotificationsEnabled(true);
					toggleItem.setData("on");
					toggleItem.setText("Turn notifications OFF");
				}
			}
		});
		
		MenuItem configItem = new MenuItem(menu, SWT.PUSH);
		configItem.setText("Configure Notifications");
		configItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Configuration.showGUI(application);
			}
		});
		
		// quit button
		MenuItem quitItem = new MenuItem(menu, SWT.PUSH);
		quitItem.setText("Quit");
		quitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.getDisplay().dispose();
                System.exit(0);
			}
		});
		
		// add listener
		trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				menu.setVisible(true);
			}
		});
	}
	
	public void showAlert(String title, String text) {
		MessageBox msg = new MessageBox(shell);
		
		msg.setText(title);
		msg.setMessage(text);
		
		msg.open();
	}
	
	public void showToolTip(String text) {
		final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
		tip.setText(text);
		trayItem.setToolTip(tip);
		tip.setVisible(true);
	}
}
