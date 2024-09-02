// Swathie Sureshan 501230338

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      try {
        if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }

      // loads users from a file 
      else if (action.equalsIgnoreCase("LOADUSERS")){
        String filename = "";
        System.out.print("User File: ");
        if (scanner.hasNextLine()){ 
          filename = scanner.nextLine();
        }
        try{
          ArrayList<User> users = TMUberRegistered.loadPreregisteredUsers(filename);
          tmuber.setUsers(users);
          System.out.println("Users Loaded");

        } catch (FileNotFoundException e){
          System.out.println("Users File: " + filename + " Not Found");
        } 
        catch (Exception e){ 
          return;
        }
        
      }

      // load drivers from a file 
      else if (action.equalsIgnoreCase("LOADDRIVERS")){
        String filename = "";
        System.out.print("Drivers File: ");
        if (scanner.hasNextLine()){ 
          filename = scanner.nextLine();
        }
        try{
          ArrayList<Driver>drivers = TMUberRegistered.loadPreregisteredDrivers(filename);
          tmuber.setDrivers(drivers);
          System.out.println("Drivers Loaded");
        } catch (FileNotFoundException e){
          System.out.print("Drivers File " + filename + " Not Found");
        } catch (Exception e){ 
          return;
        }

        
      }

      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.println("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        tmuber.registerNewDriver(name, carModel, license, address);
        System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s Address: %-15s", name, carModel, license, address);
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl!! Only needed when mixing strings and int/double
        }
        tmuber.registerNewUser(name, address, wallet);
        System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestRide() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        String accountID = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          accountID = scanner.nextLine();
        }
        String fromAddr = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          fromAddr = scanner.nextLine();
        }
        String toAddr = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          toAddr = scanner.nextLine();
        }
        tmuber.requestRide(accountID, fromAddr, toAddr);
        User user = tmuber.getUser(accountID);
        String name = user.getName();
        System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", name, fromAddr, toAddr);
        
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestDelivery() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        // "Restaurant: "           (string)
        // "Food Order #: "         (string)
        String userAccountId = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          userAccountId = scanner.nextLine();
        }
        String fromAddr = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          fromAddr = scanner.nextLine();
        }
        String toAddr = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          toAddr = scanner.nextLine();
        }
        String restaurant = "";
        System.out.print("Restaurant: ");
        if (scanner.hasNextLine())
        {
          restaurant = scanner.nextLine();
        }
        String foodOrNum = "";
        System.out.print("Food Order #: ");
        if (scanner.hasNextLine())
        {
          foodOrNum = scanner.nextLine();
        }
        
        tmuber.requestDelivery(userAccountId, fromAddr,toAddr,restaurant,foodOrNum);
        User user = tmuber.getUser(userAccountId);
        String name =  user.getName();
        System.out.printf("\nDelivery for: %-15s From: %-15s To: %-15s", name, fromAddr, toAddr);
        
      }
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      //else if (action.equalsIgnoreCase("SORTBYDIST")) 
      //{
        //tmuber.sortByDistance();
      //}
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int request = -1;
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        int zone = -1; 
        System.out.print("Zone: ");
        if (scanner.hasNextInt())
        {
          zone = scanner.nextInt();
        }
        tmuber.cancelServiceRequest(zone, request);
        System.out.println("Service request #" + request + "in zone " + zone +  " is cancelled");
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        // int request = -1;
        // System.out.print("Request #: ");
        // if (scanner.hasNextInt())
        // {
        //   request = scanner.nextInt();
        //   scanner.nextLine(); // consume nl
        // }
        String driverId = "";
        System.out.print("Driver Id: ");
        if (scanner.hasNextLine()){
          driverId = scanner.nextLine();
        }

        tmuber.dropOff(driverId);
        System.out.println("Driver " + driverId + " Dropping Off");
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      else if (action.equalsIgnoreCase("PICKUP")) {
        String dId = "";
        System.out.print("Drivers Id: ");
        if (scanner.hasNextLine()) {
          dId = scanner.nextLine();
        }

        tmuber.pickup(dId);

        Driver d = tmuber.getDriverById(dId);
        int zone = d.getZone();
        System.out.println("Driver "+ dId+" Picking Up in Zone " + zone);
      }
      else if (action.equalsIgnoreCase("DRIVETO")) {
        String dId = "";
        System.out.print("Drivers Id: "); 
        if (scanner.hasNextLine()) {
          dId = scanner.nextLine();
        }

        String addr = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine()) {
          addr = scanner.nextLine();
        }

        tmuber.driveTo(dId, addr);
        Driver d = tmuber.getDriverById(dId);
        int zone = d.getZone();
        System.out.println("Driver "+ dId + " Now in Zone "+zone);
      }
      
      }catch (UserAlreadyExistsException e) {
        System.out.println(e.getMessage());
      } catch (DriverAlreadyExistsException e){
        System.out.println(e.getMessage());
      } catch (DriverNotFoundException e){
        System.out.println(e.getMessage());
      } catch (UserAccountNotFoundException e){
        System.out.println(e.getMessage());
      } catch (InvalidAddressException e){
        System.out.println(e.getMessage());
      } catch ( InsufficientFundsException e){
        System.out.println(e.getMessage());
      } catch (InsufficientDistanceException e){
        System.out.println(e.getMessage());
      } catch (NoDriversAvailableException e) {
        System.out.println(e.getMessage());
      } catch (RideRequestAlreadyExistsException e){
        System.out.println(e.getMessage());
      } catch (DeliveryRequestAlreadyExistsException e){
        System.out.println(e.getMessage());
      } catch (InvalidDriverNameException e){
        System.out.println(e.getMessage());
      } catch (InvalidCarModelException e){
        System.out.println(e.getMessage());
      } catch (InvalidLicencePlateException e){
        System.out.println(e.getMessage());
      } catch (InvalidMoneyException e){
        System.out.println(e.getMessage());
      } catch (NameEmptyException e){
        System.out.println(e.getMessage());
      } catch (InvalidRequestNumberException e){
        System.out.println(e.getMessage());
      } catch (NoServiceRequestException e){
        System.out.println(e.getMessage());
      } catch (InvalidZoneException e){
        System.out.println(e.getMessage());
      }
      
      System.out.print("\n>");
    }
  }
}

