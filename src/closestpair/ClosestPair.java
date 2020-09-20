
/** A main class for the Closest Pair algorithms
 *  Programming assignment for
 *  CSI403 Algorithms and Data Structures
 *  University at Albany - SUNY
 *
 * Instructions: Implement methods: 
 * 1) getCPBruteForce()
 * 2) getCPDivideAndConquer()
 * As discussed in class and in the assignment part (a)
 *
 * MODIFY: Create method stubs for the two algorithms to be implemented.
 */
package closestpair;
import java.util.HashSet;

public class ClosestPair {

    /** TODO: IMPLEMENT
     *  A brute force method for the closest pair
     *  @returns an array of exactly the two closest points
     *  IMPORTANT: DO NOT CHANGE THIS METHOD SIGNATURE,
     *  ONLY THE BODY WILL BE CONSIDERED FOR GRADING
     */
    public static Point[] getCPBruteForce (Point[] pts)  {
    	
    	 if (pts.length < 2) return null; //if size too small return null
    	 else if (pts.length == 2) return pts; //if there are only 2 points, return the points as pair
    	 else {
                 
          Point[] closestpair = new Point[2]; //this array will store the closest pair
          double minimum_dist = (new Point(pts[0].x, pts[0].y)).dist(new Point(pts[1].x, pts[1].y)); //initialize minimum distance between 2 points
          
          //assign closest pair
          closestpair[0] = pts[0];
          closestpair[1] = pts[1];
          
          for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                  double distance = (new Point(pts[i].x, pts[i].y)).dist(new Point(pts[j].x, pts[j].y)); //find distance for each positions
                  //find closest pair
                  if (distance < minimum_dist) {
                      minimum_dist = distance;
                      closestpair[0] = pts[i];
                      closestpair[1] = pts[j];
                  }
              }
          }
          return closestpair;
      }
    }

    /** A driver for the Divide-And-Conquer method for the closest pair
     *  takes unsorted array of points, sorts them and invokes
     *  the recursive method you are required to implement
     *
     *  @returns an array of exactly the two closest points
     *  IMPORTANT: DO NOT CHANGE THIS METHOD
     *
     */
    public static Point[] getCPDivideAndConquer(Point[] pts) {
        Point[] ptsX = Point.sortByX(pts);
        Point[] ptsY = Point.sortByY(pts);
        return getCPDivideAndConquer(ptsX, ptsY);
    }
    

    /** TODO: IMPLEMENT
     *  A Divide-And-Conquer method for the closest pair
     *  takes as input the points sorted by increasing x
     *  and y coordinates in ptsX and ptsY respectively
     *  @returns an array of exactly the two closest points.
     *  IMPORTANT: DO NOT CHANGE THIS METHOD SIGNATURE,
     *  ONLY THE BODY WILL BE CONSIDERED FOR GRADING
     */
    public static Point[] getCPDivideAndConquer(Point[] ptsX, Point[] ptsY) {
    	
    	int num_of_pts = ptsX.length;

    	//base case
    	 if (num_of_pts <= 3) {
             return getCPBruteForce(ptsX); //Apply brute force algorithm when size is small
         } 
    	 else {
    	     
         /* Divide */

         /*Break the problem into smaller pieces to find closest pair more efficiently*/
         Point[] leftX = new Point[(num_of_pts/2)];
         Point[] rightX = new Point[num_of_pts - (num_of_pts/2)];
         
         Point[] leftY = new Point[(num_of_pts/2)];
         Point[] rightY = new Point[num_of_pts - (num_of_pts/2)];

       //append elements in pts to the half lists of pts
         for (int i = 0; i < (num_of_pts/2); i++) {
             leftX[i] = ptsX[i];
             leftY[i] = ptsY[i]; }
         for (int i = (num_of_pts/2); i < num_of_pts; i++) {
             rightX[i - (num_of_pts/2)] = ptsX[i];
             rightY[i - (num_of_pts/2)] = ptsY[i]; }
         
         /*Conquer: Two recursive calls, one to find the closest pair of points in Pair Left and the other to find the closest pair of points in Pair Right */
         //Find the closest pair in broken pieces of right and left halves of points X and Y
         Point[] closestpair_left = getCPDivideAndConquer(leftX, leftY);
         double minimum_dist_left = (new Point(closestpair_left[0].x, closestpair_left[0].y)).dist(new Point(closestpair_left[1].x, closestpair_left[1].y));
         
         Point[] closestPair_right = getCPDivideAndConquer(rightX, rightY);
         double minimum_dist_right = (new Point(closestPair_right[0].x, closestPair_right[0].y)).dist(new Point(closestPair_right[1].x, closestPair_right[1].y));  
         
         Point[] closestpair = new Point[2];
         double minimum_dist = 0; 
         
         //Find the closest pair and smallest distance
         if (minimum_dist_left < minimum_dist_right) {
             closestpair = closestpair_left;
             minimum_dist = minimum_dist_left;} 
         else {
             closestpair = closestPair_right;
             minimum_dist = minimum_dist_right;}

         /*Merge*/
           /*For each point p in the array Y′, the algorithm tries to find points in Y′ 
            * that are within δ units of p. Only the 7 points in Y′ 
            * that follow p need be considered. The algorithm computes the distance from p to 
            * each of these 7 points and keeps track of the closest-pair distance δ′ found over 
            * all pairs of points in Y′. source: http://serverbob.3x.ro/IA/DDU0221.html*/

         HashSet<Point> pairhashset = new HashSet<Point>();
         
         double left_bound_point = ptsX[(num_of_pts/2)].x - minimum_dist;
         double right_bound_point = ptsX[(num_of_pts/2)].x + minimum_dist;
         
           for (int i = 0; i < num_of_pts; i++) {
               if (ptsY[i].x < right_bound_point && left_bound_point < ptsY[i].x) {
                   pairhashset.add(ptsY[i]);                  
               }
           }

           Point[] mergedpair = new Point[pairhashset.size()];
           pairhashset.toArray(mergedpair);
           
         //Split pair with distance less than delta. Divide And Conquer Algorithm computes the distance from p to each of 7 points
           for (int i = 0; i < (pairhashset.size()) - 1; i++) {
               for (int j = i + 1; j < Math.min(pairhashset.size(), i + 7); j++) {
                 double distance = (new Point(mergedpair[i].x, mergedpair[i].y)).dist(new Point(mergedpair[j].x, mergedpair[j].y));
                   if (distance < minimum_dist) {
                	   minimum_dist = distance;
                       closestpair[0] = mergedpair[i];
                       closestpair[1] = mergedpair[j];
                   }
               }
           }
           return closestpair;
       }
    }
}