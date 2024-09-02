// Swathie Sureshan 501230338
import java.util.Arrays;
import java.util.Scanner;

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap
{
  // Checks for string consisting of all digits
  // An easier solution would use String method matches()
  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  // Get all parts of address string
  // An easier solution would use String method split()
  // Other solutions are possible - you may replace this code if you wish
  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    sc.close();
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  /**
   * @param address
   * @return
   */
  public static boolean validAddress(String address)
  {
    // Fill in the code
    // Make use of the helper methods above if you wish
    // There are quite a few error conditions to check for 
    // e.g. number of parts != 3
    int[] block = {-1, -1};

    String[] part = getParts(address);
    if (part.length != 3){
      return  false;
    }  

    boolean streetType = false;    

    // street or avenue check
    if (!part[2].equalsIgnoreCase("street") && !part[2].equalsIgnoreCase("avenue")){
      return false;
    } 
    // which one is it?
    if (part[2].equalsIgnoreCase("street"))
      streetType = true;

    // all digits and digit count == 2
    if (!allDigits(part[0]) || (part[0].length() != 2)){
      return false;
    }

    // Get first digit of street number
    int num1 = Integer.parseInt(part[0])/10;
    if (num1 == 0) return false;
   
    // Must be 'n'th or 1st or 2nd or 3rd
    String suffix = part[1].substring(1);
    if (part[1].length() != 3) 
      return false;
   
    if (!suffix.equals("th") && !part[1].equals("1st") &&
        !part[1].equals("2nd") && !part[1].equals("3rd"))
      return false;

    String digitStr = part[1].substring(0, 1);
    if (!allDigits(digitStr))
      return false;
    int num2 = Integer.parseInt(digitStr);
    if (num2 == 0)
      return false;
    if (streetType)
    {
      block[0] = num2;
      block[1] = num1;
    }
    else
    {
      block[0] = num1;
      block[1] = num2;
    }
    return true;
    
    
  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};

    // Fill in the code
    String [] part  = getParts(address);

    if (part[2].equals( "avenue" )){
      block[0] = Integer.parseInt(part[1].substring(0, 1));
      block[1] = Integer.parseInt(part[0].substring(0,1));
    } else{
      block[0] = Integer.parseInt(part[0].substring(0, 1));
      block[1] = Integer.parseInt(part[1].substring(0, 1));
    }

    return block;
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  // Hint: be careful not to generate negative distances
  
  // This skeleton version generates a random distance
  // If you do not want to attempt this method, you may use this default code
  public static int getDistance(String from, String to)
  {
    // Fill in the code or use this default code below. If you use
    // the default code then you are not eligible for any marks for this part
    int [] fblock = getCityBlock(from);
    int [] tblock = getCityBlock(to);

    int [] distance = {-1, -1};
    distance[0] = Math.abs(tblock[0]-fblock[0]);
    distance[1] = Math.abs(tblock[1]-fblock[1]);

    return distance[0] + distance[1];
  }

  // static method getCityZone 
  public static int getCityZone(String address){
    int zone = -1;
    if (!validAddress(address))
      return zone;
    else {
      int[] block = getCityBlock(address);
      int street = block[1];
      int avenue = block[0];


      if (street >= 1 && street <= 5){
        if (avenue >= 1 && avenue <= 5){
          zone = 3;
        }
        else{
          zone = 2;
        }
      }
      else{
        if (avenue >= 1 && avenue <= 5){
          zone = 0;
        }
        else{
          zone = 1;}
      }
      
      return zone;
    }
  }
}
