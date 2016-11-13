package application;

import java.io.IOException;

import org.apache.log4j.varia.NullAppender;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		org.apache.log4j.BasicConfigurator.configure(new NullAppender());
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("StartPage.fxml"));
		Scene scene = new Scene(root, 800, 480);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Thread thread = new Thread(() -> {
			work(primaryStage);
		});
		thread.setDaemon(true);
		thread.start();
		
//		if(true) return;

	}

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			System.out.println("Main exceptoion "+e);
			e.printStackTrace();
		}
	}
	
	
	private void work(Stage primaryStage) {
		String cloudaddr = "http://mealvation.fi/hackthemeal/display/";
		String workdir = /*"assets/";*/ "/home/beta/infodisp/";
		String pagedir = /*"assets/pages/";//*/ "/home/beta/pages/";
		String dinerdb = workdir + "infodiners.db";
		String params, ini, tail;
		String age = "old";
		String canintake = "Please take your portion of ";
		SetDisplay sd = new SetDisplay(dinerdb, pagedir, primaryStage);
		ReadRadio rr = new ReadRadio(workdir + "tray");
		GetDinerDb gdb = new GetDinerDb(cloudaddr + "getdinerdb.php", dinerdb);
		if (gdb.downLoadDb()) {
			age = "new";
		}
//		sd.showPage(cloudaddr + "dispdish.php?dishid=1");
		System.out.println("Infodisplay app started with " + age + " diner db");
		while (true) {
			params = rr.getData();
			try {
				ini = params.substring(0, 1);
				tail = params.substring(2);
				switch (ini) {
				case "E":
					String[] parts = tail.split(" ");
					String element = parts[0];
					String value = parts[1];
					if (element.startsWith("orig"))
						value = "Origin: " + value;
					sd.setElement(pagedir+"laxbox.html", element, value);
					break;
				case "C":
					String dishid = tail;
					sd.showDishPage(cloudaddr + "dispdish.php?dishid=" + dishid);
					break;
				case "D":
					int dinerid = Integer.parseInt(tail);
					sd.showWelcomepage(dinerid);
					break;
				case "T":
					sd.setReady(canintake);
					break;
				case "I":
					sd.showIntake(tail);
					break;
				default:
					System.out.println("Check given first parameter\n");
				}
			} catch (Exception e) {
				System.out.println("Parameter error: " + e);
				e.printStackTrace();
			}
		}
	}
	
	
}
