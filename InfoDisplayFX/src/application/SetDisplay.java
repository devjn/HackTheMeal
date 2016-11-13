/*
 * Copyright 2016, Mealvation Oy Hack the Meal project
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package application;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import application.controllers.DishController;
import application.controllers.StartController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Class to draw graphics on device screen.
 *
 *
 * @author Pertti Harju
 */
public class SetDisplay {
    
    private final GraphicsDevice device;
    private final String dinerdb;
    private final String pagedir;
    private final SimpleDateFormat ft;
    private Date date;
    private String dinername;
    private final Stage stage;
    
    /**
    * Creates an object to display html documents.
    * 
    * @param ddb path name of diner SqLite db file.
    * @param pd directory of the local html files  
    */

    public SetDisplay(String ddb, String pd, Stage stage) {
        this.ft = new SimpleDateFormat ("E MM.dd HH:mm");
        this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        this.dinerdb = ddb;
        this.pagedir = pd;
        this.stage = stage;
    }
    
     /**
     * Displays the html document on the screen in JFrame. 
     * The page argument must specify the html file address and protocol
     * leading (file:/ or http:) in String format
     * <p> 
     * When this method attempts to draw the image on
     * the screen, the data will be loaded. The graphics primitives 
     * that draw the image will incrementally paint on the screen. 
     *
     * @param  page the location of the html file
     * @see         String
     */
    
    public void showPage(String page){
        GetInfoPage gip = new GetInfoPage();
        JFrame cframe = new JFrame();
        cframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        cframe.setUndecorated(true);
        cframe.add(gip.getPage(page));
        cframe.pack();
        cframe.setVisible(true);
//        device.setFullScreenWindow(cframe);
    }
    
    /**
	 * @param page
	 */
	public void showDishPage(String page) {
		Document doc = null;
		String meal = "";
		String diet = "";
		String energy = "";
		String origin = "";
		String uri = "http://mealvation.fi/hackthemeal/display/2_img.jpg";
		// int status = 0;
		// String allerg = "";
		// String diet ="My diets: ";
		System.out.println("showDishPage");
		DishController controller = new DishController();
		try {
			doc = Jsoup.connect(page).get();
			for (Element e : doc.select("img[src~=(?i)\\.(jpe?g)]")) {
				System.out.println(e.attr("src"));
				uri = "http://mealvation.fi/hackthemeal/display/" + e.attr("src");
			}
			Image image = new Image(uri, 350, 450, true, true);
			controller.imageDish.setImage(image);
			meal = doc.getElementById("meal").text();
			diet = doc.getElementById("diet").text();
			energy = doc.getElementById("energy").text();
			origin = doc.getElementById("origin").text();

			controller.dishName.setText(meal);
			controller.lbDiet.setText(diet);
			controller.lbEnergy.setText(energy);
			controller.lblOrigin.setText(origin);

			System.out.println("showDishPage dishName " + meal);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// http://mealvation.fi/hackthemeal/display/

		show(controller);
	}
    
	public void show(Parent controller) {
		Platform.runLater(() -> {
			Scene scene = new Scene(controller, 800, 480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			stage.hide();
			stage.setScene(scene);
			// stage.setTitle("Custom Control");
			// stage.setFullScreen(true);
			stage.show();
			System.out.println("Platform.runLater "+scene);
		});
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
     * Sets value of element inside page documnet and writes new document to 
     * file live.html and displays it. 
     * <p> 
     * @param  page the location of the html file
     * @param  element name of the element in document
     * @param  val for element
     * @see         String
     */    
    public void setElement(String page, String element, String val) {
        //print("Fetching %s...", page);
        Document doc = null;
        try {
            File input = new File(page);
            doc = Jsoup.parse(input, "UTF-8", "");      
            doc.getElementById(element).text(val);
            File livefile = new File(pagedir + "live.html");
            if (!livefile.exists()) {
                livefile.createNewFile();
            }
            FileWriter fw = new FileWriter(livefile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(doc.html());
            bw.close();
        } catch (IOException ex) {
           System.out.println("prerr" + ex );
        }
        showPage("file://" + pagedir + "live.html");
    }

    /**
     * Sets new value for take element in wdlive.html file and displays altered file. 
     * <p> 
     * @param  take advise to take paortion
     * @see         String
     */  
    
    public void setReady(String take) {
        Document doc = null;
        String meal = getMeal();
        try {
            File input = new File(pagedir + "wdlive.html");
            doc = Jsoup.parse(input, "UTF-8", "");      
            doc.getElementById("take").text(take + meal);
            File livefile = new File(pagedir + "wdlive.html");
            if (!livefile.exists()) {
                livefile.createNewFile();
            }
            FileWriter fw = new FileWriter(livefile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(doc.html());
            bw.close();
        } catch (IOException ex) {
           System.out.println("prerr" + ex );
        }
        showPage("file://" + pagedir +"wdlive.html");
    }
    
    /**
     * Populates wdlive.html with values and displays it to inform diner when recognized. 
     *
     * Values to display are queried from dinerdb,
     * diner name is saved for private use in class. 
     * 
     * <p> 
     * @param  dinerid  id of diner in infodisplay.db 
     * @throws IOException 
     */ 

    public void showWelcomepage (int dinerid) {
        Connection c = null;
        Statement stmt = null;
        String  fname = "";
        String  lname = "";
        int energyneed = 0;
        int lac = 0, glu = 0, mil = 0, vl = 0, veg = 0;
        int status = 0;
        String allerg = ""; 
        String diet ="My diets: ";
        StartController startController = new StartController();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dinerdb);
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM diner WHERE id = " + dinerid + ";" );
            while (rs.next()) {
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                energyneed  = rs.getInt("energyneed");
                lac = rs.getInt("L");
                glu = rs.getInt("G");
                mil = rs.getInt("M");
                vl = rs.getInt("VL");
                veg = rs.getInt("V");
                status = rs.getInt("status");
                allerg = rs.getString("allergy");
            }
            if(lac == 1) diet = diet +"L ";
            if(glu == 1) diet = diet +"G ";
            if(mil == 1) diet = diet +"M ";
            if(vl == 1) diet = diet +"VL";
            if(veg == 1) diet = diet +"V ";
            rs.close();
            stmt.close();
            c.close();
            dinername = fname;
            date = new Date();
            
            startController.labelBottom.setText("Restaurant SMART " + ft.format(date));
            startController.lblWelcome.setText("Welcome again " + fname);
            startController.lblDiet.setText(diet);
            startController.lblEnergy.setText("Daily energy need: " + Integer.toString(energyneed) + " kcal");
            
//        	Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Start.fxml"));
            
//            File input = new File(pagedir + "welcome.html");
//            doc = Jsoup.parse(input, "UTF-8", "");
//            doc.getElementById("myheader").text("Restaurant SMART " + ft.format(date));
//            doc.getElementById("diner").text("Welcome again " + fname);
//            doc.getElementById("diet").text(diet);
//            doc.getElementById("energy").text("My daily energyneed is " + Integer.toString(energyneed) + " kcal");
//            File livefile = new File(pagedir + "wdlive.html");
//            if (!livefile.exists()) {
//                livefile.createNewFile();
//            }
//            FileWriter fw = new FileWriter(livefile.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(doc.html());
//            bw.close();
        } catch ( ClassNotFoundException | SQLException e ) {
          System.out.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("some point");
        show(startController);
//        showPage("file:///E|/MyFiles/Hack/hackthemeal/HackTheMeal/InfoDisplay/" + pagedir + "wdlive.html");
    }
    
    /**
     * Populates intake.html with new values, save them to liveintake.html
     * and displays it to inform diner from intake amounts. 
     * <p> 
     * @param  intake  amount of intake 
     * 
     * @see     String
     */ 
    
    public void showIntake(String intake){
        date = new Date();
        Document doc = null;
        String kcal = String.valueOf(Integer.parseInt(intake) * getEnergy()/100) + " kcal";
        try{
            File input = new File(pagedir + "intake.html");
            doc = Jsoup.parse(input, "UTF-8", "");
            doc.getElementById("myheader").text("Restaurant SMART " + ft.format(date));
            doc.getElementById("diner").text("Your intake " + dinername);
            doc.getElementById("meal").text("of " + getMeal() + " is:");
            doc.getElementById("weight").text(intake + " g.");
            doc.getElementById("energy").text( kcal);
            File livefile = new File(pagedir + "liveintake.html");
            if (!livefile.exists()) {
                livefile.createNewFile();
            }
            FileWriter fw = new FileWriter(livefile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(doc.html());
            bw.close();
        }
        catch (IOException e){
            System.out.println("Intake err " + e);
        }
        showPage("file://"+ pagedir + "liveintake.html");
    }
    
    /**
     * Gets meal name inside file live.html. 
     * <p>
     * @return meal name
     * @see         String
     */
    
    public String getMeal(){
        Document doc = null;
        String meal = "";
        try {
            File input = new File(pagedir + "live.html");
            doc = Jsoup.parse(input, "UTF-8", "");
            Element link = doc.getElementById("meal");
            meal = link.text();
        } catch (IOException ex) {
           System.out.println("getmealerr" + ex );
        }
        return meal;
    }
    
    /**
    * Gets energy as kcal/100g of the meal inside file live.html. 
    * <p> 
    * @return energy
    * @see         int
    */
    
    public int getEnergy(){
        Document doc = null;
        int energy = 0;
        try {
            File input = new File(pagedir + "live.html");
            doc = Jsoup.parse(input, "UTF-8", "");
            Element link = doc.getElementById("energy");
            energy = Integer.parseInt(link.text());
        } catch (IOException ex) {
           System.out.println("getenergyerr" + ex );
        }
        return energy;
    }
}

    

