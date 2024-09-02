// Swathie Sureshan 501230338
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import java.util.Iterator;

import java.util.Map;


/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map<String, User> users;
  private ArrayList<User> userList;
  private ArrayList<Driver> drivers;

  private Queue<TMUberService>[] serviceQueues; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new TreeMap<>();
    userList = new ArrayList<>();
    drivers = new ArrayList<Driver>();
    serviceQueues = new Queue[4];
    
    for (int i=0; i<4; i++) {
      serviceQueues[i] = new LinkedList<>();
    }
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }

  public String generateUserAccountId(){
    return "" + userAccountId + users.size();
  }
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    // Fill in the code
    return users.get(accountId);
  }
  // setUser method
  public void setUsers(ArrayList<User> userList){
    users.clear();
    for (User user : userList){
      users.put(user.getAccountId(), user);
      this.userList.add(user);
    }
  }

  // set driver method
  public void setDrivers(ArrayList<Driver> drivers){
    this.drivers.clear();
    this.drivers.addAll(drivers);
  }

  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Fill in the code
    return users.containsValue(user);
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 {
   // Fill in the code
   for (Driver d: drivers){
    if (d.equals(driver))
      return true;
   }
   return false;
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    // Fill in the code
    for (Queue<TMUberService>  queue : serviceQueues){
      for (TMUberService service : queue){
        if (service.equals(req))
          return true;
      }
    }
    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {
    // Fill in the code
    
    for (Driver d : drivers){
      if (d.getStatus().equals(Driver.Status.AVAILABLE)){
        return d;
      }
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    for (int i = 0; i < userList.size(); i++){
      int index = i+1;
      System.out.printf("%-2s. ", index);
      userList.get(i).printInfo();
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    // Fill in the code
    System.out.println();
    
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i + 1;
      System.out.println();
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      if (drivers.get(i).getStatus() == Driver.Status.DRIVING){
        TMUberService service = drivers.get(i).getService();
        System.out.println("\nFrom: " + service.getFrom() + "\tTo: " + service.getTo());
      }
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    // Fill in the code
    for (int i = 0; i < serviceQueues.length; i++){
      Queue<TMUberService> queue = serviceQueues[i];
      System.out.println("\nZONE " + i);
      System.out.println("======");

      int count = 1;

      for (TMUberService service : queue){
        System.out.println("");
        System.out.printf("%-1s. %-2s ", count, "------------------------------------------------------------");
        service.printInfo();
        System.out.println();
        count++;
      }
    }
  }

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    //    set errMsg string variable to "Invalid Address "
    //    return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    if (name == null || name == ""){
      throw new NameEmptyException("Name cannot be empty");
    } 
    if (CityMap.validAddress(address) == false){
      throw new InvalidAddressException("Invalid Address");
    } 
    if (wallet < 0){
      throw new InvalidMoneyException("Invalid money in wallet");
    } 

    User  u = new User(generateUserAccountId(), name, address, wallet);

    if (userExists(u)){
      throw new UserAlreadyExistsException("User Already Exists in System");
    } 
    users.put(generateUserAccountId(), u);
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address)
  {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    String newId =  TMUberRegistered.generateDriverId(drivers);
    Driver d  = new Driver(newId ,name ,carModel, carLicencePlate, address);
    if (name  == null || name == "" ) {
      throw new InvalidDriverNameException("Invalid Driver Name"); 
    } 
    if (carModel == null || carModel == ""){
      throw new InvalidCarModelException("Invalid Car Model");  
    }
    if (carLicencePlate == null || carLicencePlate == ""){
      throw new InvalidLicencePlateException("Invalid Licence Plate");
    }
    if (driverExists(d)){
      throw new DriverAlreadyExistsException("Driver already exists in the system.");
    }
    drivers.add(d);
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Check for valid parameters
	// Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user at a time!
    // Change driver status
    // Add the ride request to the list of requests
    // Increment the number of rides for this user
    User user = getUser(accountId);

    if  (user == null){
        throw new UserAccountNotFoundException("User Account Not Found");
    }
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
      throw new InvalidAddressException("Invalid Address");
    }
    int dist = CityMap.getDistance(from, to);
    if (dist <= 1){
      throw new InsufficientDistanceException("Insufficient travel distance");
    }
    Driver d = getAvailableDriver();
    if (d==null){
      throw new NoDriversAvailableException("No Drivers Available");
    }
    double cost = getRideCost(dist);
    if (cost >  user.getWallet()){
      throw new InsufficientFundsException("Insufficient funds");
    }

    // rr = request
    TMUberRide rr = new TMUberRide(d,from, to, user, dist, getRideCost(dist)); // no cost was added above yet ( how do you calculate cost of the travel)
    if (existingRequest(rr)){
      throw new RideRequestAlreadyExistsException("User Already has ride request");
    }

    int zone = CityMap.getCityZone(from);
    serviceQueues[zone].add(rr);
    user.addRide();
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food order id
    // Increment the number of deliveries the user has had
    User user  = getUser(accountId);
    if (user == null) {
      throw new UserAccountNotFoundException("User Account Not Found");
    }

    int dist = CityMap.getDistance(from, to);
    if (dist <= 1){
      throw new InsufficientDistanceException("Insufficient travel distance");
    }

    Driver d = getAvailableDriver();
    if (d==null){
      throw new NoDriversAvailableException("No Drivers Available");
    }

    double cost = getDeliveryCost(dist);
    if (cost >  user.getWallet()){
      throw new InsufficientFundsException("Insufficient funds");
    }

    TMUberDelivery dr = new TMUberDelivery(d, from, to, user, dist, getDeliveryCost(dist), restaurant, foodOrderId);
    if (existingRequest(dr)){
      throw new DeliveryRequestAlreadyExistsException("User Already has delivery request at restaurant with this food order");
    }

    int zone = CityMap.getCityZone(from);
    serviceQueues[zone].add(dr);
    user.addDelivery();
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public void cancelServiceRequest(int zone,int request) 
  {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    if (request < 1 || request >  serviceQueues.length){
      throw new InvalidRequestNumberException("Invalid request #");
    }

    // check if valid zone 
    if (zone < 0 || zone > 3){ 
      throw new InvalidZoneException("Invalid Zone");
    }

    Iterator<TMUberService> iter = serviceQueues[zone].iterator();
    int index = 1;
    TMUberService requestToCancel = null;

    while(iter.hasNext()) {
      requestToCancel = iter.next();
      if (index == request){
        break;
      }
      index++;
    }

    User userRequesting = requestToCancel.getUser();

    if (requestToCancel.getServiceType().equals("RIDE")){
      userRequesting.decrementRide();
    }
    else{
      userRequesting.decrementDelivery();
    }

    serviceQueues[zone].remove(requestToCancel);

  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(String driverId)
  {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenue
    // Change driver status
    // Deduct cost of service from user
    Driver driver = null;
    for (Driver d : drivers){
      if (d.getId().equals(driverId)){
        driver = d;
        break;
      }
    }

    if (driver == null){
      throw new DriverNotFoundException("Driver  not found");
    }

    TMUberService service = driver.getService();
    double driverPay = service.getCost() * PAYRATE;
    double cost = service.getCost();
    totalRevenue += cost;

    driver.pay(driverPay);
    totalRevenue -= driverPay;

    User user =   service.getUser();
    user.payForService(cost);

    driver.setStatus(Driver.Status.AVAILABLE);
    driver.setService(null);

    String dropOffAddr = service.getTo();
    driver.setAddress(dropOffAddr);
    int driverZone = CityMap.getCityZone(dropOffAddr);
    driver.setZone(driverZone);
    return true;
  }

  // pickup method
  public void pickup(String driverId){
    Driver driver = null;
    for (Driver d : drivers){
      if (d.getId().equals(driverId)){
        driver = d;
        break;
      }
    }

    if (driver == null){
      throw new DriverNotFoundException("Driver not found");
    }

    String driverAddress = driver.getAddress();
    int zone = CityMap.getCityZone(driverAddress);

    Queue<TMUberService> queue = serviceQueues[zone];
    if (queue.isEmpty()) {
      throw new NoServiceRequestException("\nNo Service Request in Zone " + zone);
    }

    TMUberService service  = queue.remove();
    driver.setService(service);
    driver.setStatus(Driver.Status.DRIVING);
    driver.setAddress(service.getFrom());
  }

  // driveto method
  public void driveTo(String driverId, String address){
    Driver driver = null;
    for (Driver d : drivers){
      if (d.getId().equals(driverId)){
        driver = d;
        break;
      }
    }

    if (driver == null){
      throw new DriverNotFoundException("Driver not found");
    }

    if (driver.getStatus() != Driver.Status.AVAILABLE){
      throw new NoDriversAvailableException("Driver not available");
    }

    if (!CityMap.validAddress(address)){
      throw new InvalidAddressException("Invalid Address");
    }

    driver.setAddress(address);
    int driverZone = CityMap.getCityZone(address);
    driver.setZone(driverZone);
  }

  public Driver getDriverById (String dId) {
    for (Driver d: drivers) {
      if (d.getId().equals(dId)) {
        return d;
      }
    }
    return null;
  }
  // Sort users by name
  // Then list all users
  public void sortByUserName()
  {
    Collections.sort(userList, new NameComparator());
      
    listAllUsers();
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User> {
    public int compare(User u1, User u2){ 
      return u1.getName().compareTo(u2.getName());
    }
  }

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet()
  {
    Collections.sort(userList, new UserWalletComparator());

    listAllUsers();
  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements  Comparator<User>
  {
    public int compare(User u1, User u2){
      if (u1.getWallet() > u2.getWallet()) return 1;
      else if (u1.getWallet() < u2.getWallet()) return -1;
      else return 0;
    }
      
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  //public void sortByDistance()
  //{
    //Collections.sort(serviceRequests);
    //listAllServiceRequests();
  //}

}


class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException (String errMsg) {
    super ( errMsg);
  }
}

class DriverAlreadyExistsException extends RuntimeException {
  public DriverAlreadyExistsException (String errMsg) {
    super ( errMsg);
  }
}

class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException (String errMsg) {
    super ( errMsg);
  }
}

class UserAccountNotFoundException extends RuntimeException {
  public UserAccountNotFoundException (String errMsg) {
    super ( errMsg);
  }
}

class InvalidAddressException extends IllegalArgumentException {
  public  InvalidAddressException (String errMsg) {
    super (errMsg);
  }
}

class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException (String errMsg) {
    super ( errMsg);
  }
}

class InsufficientDistanceException extends RuntimeException{
  public  InsufficientDistanceException(String errMsg){
    super(errMsg);  
  }
}

class NoDriversAvailableException extends IllegalArgumentException {
  public  NoDriversAvailableException(String errMsg) {
    super(errMsg);
  }
}

class RideRequestAlreadyExistsException extends RuntimeException{
  public  RideRequestAlreadyExistsException(String errMsg) {
    super(errMsg);
  }
}

class DeliveryRequestAlreadyExistsException extends RuntimeException{
  public  DeliveryRequestAlreadyExistsException(String errMsg) {
    super(errMsg);
  }
}

class InvalidDriverNameException extends  IllegalArgumentException {
  public  InvalidDriverNameException(String errMsg) {
    super(errMsg);
  }
}

class InvalidCarModelException extends   IllegalArgumentException {
  public   InvalidCarModelException(String errMsg) {
    super(errMsg);
  }
}

class InvalidLicencePlateException extends   IllegalArgumentException {
  public   InvalidLicencePlateException(String errMsg) {
    super(errMsg);
  }
}

class InvalidMoneyException  extends  ArithmeticException{
  public   InvalidMoneyException(String errMsg) {
    super(errMsg);
  }
}

class NameEmptyException  extends RuntimeException {
  public NameEmptyException(String errMsg) {
    super(errMsg);
  }
}

class InvalidRequestNumberException extends  IllegalArgumentException{
  public  InvalidRequestNumberException(String errMsg){
    super(errMsg);
  }
}

class NoServiceRequestException extends IllegalArgumentException{
  public  NoServiceRequestException(String errMsg) {
    super(errMsg);
  }
}

class InvalidZoneException extends RuntimeException{
  public InvalidZoneException(String errMsg){ 
    super(errMsg);
  }
}