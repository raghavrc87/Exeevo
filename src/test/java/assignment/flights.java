package assignment;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

//import testBase.setDriver;

//import io.github.bonigarcia.wdm.WebDriverManager;

public class flights {


    @Test(dataProvider="getflights", dataProviderClass = datasupply.class )
    public void openchrome(String data) throws Exception {

        //Extend Reports initialisations
        ExtentSparkReporter reporter = new ExtentSparkReporter("myreport1.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        ExtentTest test = extent.createTest("Select destination and dates");

        //Spltting the data provider supplied data for further use
        String user[]=data.split("/");
        String from = user[0];
        String to = user[1];
        String dpDt=user[2];
        //String rtDt=user[3];

        System.out.println("From: "+user[0]);
        System.out.println("To: "+user[1]);
        System.out.println("Dep Date: "+ user[2]);
        System.out.println("-----------");
        //System.out.println(user[3]);


        //Below tried using proxies,ChromeOptions,switches since MakeMyTrip blocked my automation code from clicking on Search button, unsuccessful.
		/*
		 * String proxyAddress = "192.168.4.3"; int proxyPort = 9389;
		 *
		 * // Create a Proxy object and set the HTTP and SSL proxies Proxy proxy = new
		 * Proxy();
		 *
		 * proxy.setHttpProxy("192.168.4.3:4444"); //proxy.setSslProxy(proxyAddress +
		 * ":" + proxyPort);


			//ChromeOptions options = new ChromeOptions();
			//options.setProxy(proxy);
			//options.addArguments("disable-infobars");
			//options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		 */


        //Adding desired capabilities and selenium server url in order for tests to run in Selenium Grid , parallely.
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");

        WebDriver driver = new RemoteWebDriver(new URL("http://192.168.1.3:4444"),caps); //Selenium Server url


        //PageFactory.initElements(driver,this);
        driver.get("https://www.makemytrip.com"); //launch website
        test.addScreenCaptureFromPath(capturescreenshot(driver));
        driver.findElement(By.xpath("//a[@href='https://www.makemytrip.com/flights/']")).click();; //click on flights
        test.addScreenCaptureFromPath(capturescreenshot(driver));

        driver.findElement(By.xpath("//li[@data-cy='roundTrip']")).click(); //click on round trip
        test.addScreenCaptureFromPath(capturescreenshot(driver));

        driver.findElement(By.id("fromCity")).sendKeys(from); //enter 'from city'
        test.addScreenCaptureFromPath(capturescreenshot(driver));
        Thread.sleep(2000);

        Actions actions = new Actions(driver); //arrow down once for selecting first result
        actions.pause(Duration.ofSeconds(2)).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        test.addScreenCaptureFromPath(capturescreenshot(driver));

        driver.findElement(By.id("toCity")).sendKeys(to); //enter 'To city'

        //arrow down once for selecting first result
        actions.pause(Duration.ofSeconds(2)).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        test.addScreenCaptureFromPath(capturescreenshot(driver));

        actions.sendKeys(Keys.ESCAPE).build().perform(); //Pressing escape to be able to click on departure, else Click intercepted exception occurs because entering 'To City' open to departure date automatically

        Thread.sleep(5000);
        driver.findElement(By.xpath("//p[@data-cy='departureDate']")).click(); //click on departure date calendar
        test.addScreenCaptureFromPath(capturescreenshot(driver));
        //driver.findElement(By.id("departure")).click();
        //input[@id='departure']
        Thread.sleep(2000);

        //Catching the element that has our departure date
        WebElement depdate = driver.findElement(By.xpath("//div[contains(@aria-label, '"+dpDt+"') and @aria-disabled='false']"));
        depdate.click();
        test.addScreenCaptureFromPath(capturescreenshot(driver));
        String stdepdate = depdate.getAttribute("aria-label");
        System.out.println(stdepdate);
        Date date1=new SimpleDateFormat("E MMM dd yyyy").parse(stdepdate);
        System.out.println(date1.toString());

        //adding 7 days to the departure date
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, 7);
        Date date2 = c.getTime();
        //Date date2=new SimpleDateFormat("E MMM dd yyyy").format(c.getTime());

        //String newDate = sdf.format(c.getTime());
        //Displaying the new Date after addition of Days
        //System.out.println("Date after Addition: "+newDate);

        //Converting the returning date into the MakeMyTrip webelement format, so that we can capture it and click on it
        String year = date2.toString().substring(date2.toString().length()-4, date2.toString().length());
        //System.out.println(date1.toString().substring(0, date1.toString().length()-17));
        String trimmedDate = date2.toString().substring(0, date2.toString().length()-18);
        String rtDt = trimmedDate+" "+year;

        //Now capturing and clicking the return date webelement, created above
        WebElement returndate = driver.findElement(By.xpath("//div[contains(@aria-label, '"+rtDt+"') and @aria-disabled='false']"));
        returndate.click();
        test.addScreenCaptureFromPath(capturescreenshot(driver));
        //extent.flush();
        test.pass("Test Passed.");
        //test.addScreenCaptureFromPath(capturescreenshot(driver));

        extent.flush();
        driver.close();
        // --------- Logic for whichever date in the next month(only next month)
        //String[] tokens = stdepdate.split(" ",2);
        //System.out.println(tokens[1]);
        //System.out.println("reutnr"+rtDt);
        //Date date1=new SimpleDateFormat("dd-MMM-yyyy").parse(rtDt);
        //System.out.println(date1);
        //System.out.println(date1.toString().substring(date1.toString().length()-4, date1.toString().length()));
        //String year = date1.toString().substring(date1.toString().length()-4, date1.toString().length());
        //System.out.println(date1.toString().substring(0, date1.toString().length()-17));
        //String trimmedDate = date1.toString().substring(0, date1.toString().length()-18);
        //System.out.println(trimmedDate+" "+year);
        //Date date1=new SimpleDateFormat("MMM dd yyyy").parse(stdepdate);
        //System.out.println(date1);
        //Thread.sleep(5000);
        //driver.quit();
        //////p[@data-cy='departureDate'] //calendar departure click
        ////div[contains(@aria-label, '2024') and @aria-disabled='false'] //all Enabled dates
        //a[@href='https://www.makemytrip.com/flights/']
        //driver.findElement(By.xpath("//p[text()='Indira Gandhi International Airport']")).click();
        //driver.findElement(By.xpath("//span[text()='HDO']")).click();
        //// splitview two divs - departure , return = //div//div[@class="paneView"]

        //Thread.sleep(5000);
        //driver.findElement(By.xpath("//a[@class='primaryBtn font24 latoBold widgetSearchBtn ']")).click(); //search click
        //// search button //a[@class='primaryBtn font24 latoBold widgetSearchBtn ']
        ////span[text()='HDO']
        // new delhi = //p[text()='Indira Gandhi International Airport']
        //round trip = //li[@data-cy='roundTrip']
        //driver.findElement(By.xpath("//a[@href='/mobile-phones-store?fm=neo%2Fmerchandising&iid=M_1bd73a9f-ff34-43e2-86ba-f57e77423377_1_372UD5BXDFYS_MC.ZRQ4DKH28K8J&otracker=hp_rich_navigation_2_1.navigationCard.RICH_NAVIGATION_Mobiles_ZRQ4DKH28K8J&otracker1=hp_rich_navigation_PINNED_neo%2Fmerchandising_NA_NAV_EXPANDABLE_navigationCard_cc_2_L0_view-all&cid=ZRQ4DKH28K8J']")).click();
    }

    public String capturescreenshot(WebDriver driver) throws Exception {

        File srcfile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destinationfilepath = new File("src/../images/screenshots"+System.currentTimeMillis()+".jpeg");
        String absolutefilelocation = destinationfilepath.getAbsolutePath();
        FileUtils.copyFile(srcfile, destinationfilepath);
        return absolutefilelocation;
    }
}
