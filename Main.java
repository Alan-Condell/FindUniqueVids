import java.io.File;
import java.util.*;
 /**
  * @author Alan.Condell
  * This program can be used to search for a video for each combination of dog positions in a 6 trap race.
  * Video files must be in the following format -- TTT_YYYY_MM_DD_RN_PPPPPP
  * Where: 
  *        TTT    = Track Code      -> LMK
  * 	   YYYY   = Year            -> 2019
  * 	   MM     = Month           -> 01
  * 	   DD     = Day             -> 21
  * 	   RR     = Race Number     -> 02
  * 	   PPPPPP = Dog placements  -> 421365
  */

public class Main {
	/**
	 * Class Main
	 * 
	 * @param ArrayList<String> foundVids            -- Stores all found video combinations
	 * @param ArrayList<String> notFoundVids         -- Stores all not found video combinations
	 * @param ArrayList<String> errorInDogPlacements -- Stores all videos with error in dog placements e.g 123455 or 643451
	 * @param ArrayList<Integer> possibilities       -- Stores all 720 possible out comes for the digits 1 2 3 4 5 6
	 * 
	 * @param File videosDir                         -- Stores the directory where the videos are located
	 * @param File selectedVideoDir                  -- Stores the directory where the videos are to be moved to
	 * 
	 */
	
	static ArrayList<String> foundVids 			  = new ArrayList<String>();
	static ArrayList<String> notFoundVids         = new ArrayList<String>();
	static ArrayList<String> errorInDogPlacements = new ArrayList<String>();
	static ArrayList<Integer> possibilities       = new ArrayList<Integer>();
	
	static File videosDir        = new File("C:\\Videos");
	static File selectedVideoDir = new File("C:\\Videos\\SelectedVideos");
	
	public static void main(String[] args) {
		/**
		 * main
		 * 
		 * @param String[] vidPaths        -- used to get all video paths in videosDir.
		 * @param String[] splitVid        -- Stores a single video file name split into its parts e.g. {LMK, 2019, 01, 01, 01, 123456.ps}
		 * @param String path              -- Stores a single video path e.g. LMK_2019_01_01_123456.ps
		 * @param String[] posAndExtension -- Stores the 6 digit placement and its file extension e.g. 123456.ps
		 * @param String pos		       -- Stores the 6 digit placement from the video path e.g. 123456
		 * 
		 */
		
		genCombos();
		
		// Checks if the videosDir and selectedVideoDir already exists.
		// if not - creates them
		try {
			if(!(videosDir.exists()))
				videosDir.mkdir();
			
			if(!(selectedVideoDir.exists()))
				selectedVideoDir.mkdir();
			
		}catch(Exception e) {
			System.out.println("Error: Could not find C: drive on PC");
		}
		System.out.println("This program can be used to search for a video for each combination of dog positions in a 6 trap race.\r\n" + 
				"Video files must be in the following format -- TTT_YYYY_MM_DD_RN_PPPPPP\r\n" + 
				"Where: \r\n" + 
				"           TTT    = Track Code      -> LMK\r\n" + 
				" 	   YYYY   = Year            -> 2019\r\n" + 
				" 	   MM     = Month           -> 01\r\n" + 
				" 	   DD     = Day             -> 21\r\n" + 
				" 	   RR     = Race Number     -> 02\r\n" + 
				" 	   PPPPPP = Dog placements  -> 421365\r\n");
		System.out.println("----------------------------------------------------------------");
		System.out.println("\nPlease make sure videos are located in " + videosDir.getAbsolutePath() +
						   "\nOR you have changed the Directories to the appropriate ones (see Doc for more details)" +
				           "\nPress ENTER to start.");
		
		// Waits for enter to be pressed
		try{
			System.in.read();
			
		} catch(Exception e){
			System.out.print(e);
		}
		System.out.println("----------------------------------------------------------------");
		
		String[] vidPaths = videosDir.list();
		String[] splitVid = new String[6];
		
		// for each path in vidPaths
        for (String path : vidPaths) {
        	
        	//if path does not contains selectedVideos
        	if(!(path.contains("SelectedVideos"))) {
        		splitVid = path.split("_");
        	
        		String pos = splitVid[5];
        		String [] posAndExtension = pos.split("\\.");

        		pos = posAndExtension[0];
        		
        		// if possibilities contains pos
        		if(possibilities.contains(Integer.parseInt(pos))) {
        			addToSelectedVideos(path, pos);
        		} else {
        			errorInDogPlacements.add(path);
        		}
            }
        }
        
        for(int i = 0; i < possibilities.size(); i++) {
        	String p = Integer.toString(possibilities.get(i));
        	
        	//if foundVids does NOT contains p
        	if(!(foundVids.contains(p)))
        		notFoundVids.add(p); 
        }

        printResults();
	}
	
	//generate all possible combinations of races using 1 2 3 4 5 6.
	private static void genCombos() {
		/**
		 * genCombos
		 * 
		 * @param int start                 -- Starting number
		 * @param int end                   -- Ending number
		 * @param String num                -- Stores a number between start and end
		 */
		
		//ArrayList<String> temp = new ArrayList<String>();
		
		int start  = 123456;
		int end    = 654321;
		
		//for all numbers from 123456 - 654321.
		for(int i = start; i <= end; i++) {
			String num = Integer.toString(i);

			//if i has 0, 7, 8, 9 in any position, don't add.
			if(!(num.contains("0") || num.contains("7") || num.contains("8") || num.contains("9"))) {
				
				//goes through each index in num, and sees if the number at that index is repeated.
				for(int j = 0; j < num.length(); j++) {
					
					// if num has full unique digits 
					if(isUnique(num)) {
						possibilities.add(Integer.parseInt((num)));
						break;
					}
				}
			}
		}
		Collections.sort(possibilities);
	}
	
	//checks generated number to see if it has any recurring digits.
	private static boolean isUnique(String num) {
		/**
		 * isUnique
		 * 
		 * @param String num     -- number passed from genCombos
		 * @param char[] chArray -- Array that holds each digit of num
		 */
		
		char[] chArray = num.toCharArray();
		Arrays.sort(chArray);
		
		for(int i = 0; i < chArray.length - 1; i++) {
			if(chArray[i] != chArray[i + 1])
				continue;
			else
				return false;
		}
		return true;
		
	}
	// find all videos in given dir and move them to new dir
	private static void addToSelectedVideos(String path, String pos) {
		/**
		 * addToSelectedVideos
		 * 
		 * @param String path -- passed from main, Stores a single video path e.g. LMK_2019_01_01_123456.ps
		 * @param String pos  -- passed from main, Stores the 6 digit placement from the video path e.g. 123456
		 * @param File video  -- Stores a video's location that is to be moved 
		 */
		
		try{
			File video = new File("C:\\Videos\\" + path);
			
		    //if pos is NOT duplicate
	    	if(!(duplicate(pos))) {
	    		
	    		//if video is 'renamed' and added to selectedVideos
	    		if(video.renameTo(new File("C:\\Videos\\SelectedVideos\\" + path))){
	    			System.out.println("File: " + path + " was moved");
	    			foundVids.add(pos);
	    		}
	    		
	    	} else	System.out.println("File: " + path + " was NOT moved (duplicate)");
	    	    
	    	} catch(Exception e){
	    		e.printStackTrace();
	    	}
	}
	
	private static boolean duplicate(String pos) {
		/**
		 * duplicate
		 * 
		 * @param String pos 			   -- passed from addToSelectedVideos, Stores the 6 digit placement from the video path e.g. 123456
		 * @param String[] allSelectedVids -- Stores all videos in the selectedVideoDir
		 * @param currentVid               -- Stores the current video being checked in allSelectedVids
		 */
		String[] allSelectedVids  = selectedVideoDir.list();

		for(String currentVid : allSelectedVids) {
			if(currentVid.contains(pos))
				return true;
		}
		return false;
	}
	
	private static void printResults() {

        System.out.println("----------------------------------------------------------------");
        System.out.println(notFoundVids.size() + " of the 720 possibilities were not found.\nThey include:");
        
        for(int i = 0; i < notFoundVids.size();  i++)
        	System.out.println("Possibility- " + notFoundVids.get(i) + " NOT Found");
        
        System.out.println("----------------------------------------------------------------");
        
        for(int i = 0; i < errorInDogPlacements.size(); i++) {
        	System.out.println("ERROR: Dog placements are incorrect.\nPlease make sure " + errorInDogPlacements.get(i) + " is named correctly");
        }
        
        System.out.println("----------------------------------------------------------------");
	}
	
	
}