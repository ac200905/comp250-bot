package bot;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rts.*;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;


public class KamikazeBot extends AbstractionLayerAI {    
    UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType heavyType;
    UnitType lightType;
    UnitType rangedType;
    
    boolean testFalse = false;
    boolean workerRush = false;
    boolean standardStrat = false;
    boolean mapSmall = false;
    boolean mapMedium = false;
    boolean mapLarge = false;
    boolean hasHarvester = false;
    boolean armyAttack = false;
    
    
    int extraHarvesters = 1;
    int maxAttackWorkers = 1;
    int buildOffsetX = 0;
    int buildOffsetY = 0;
    int rallyPointX = 0;
    int rallyPointY = 0;
    
    List<Unit> everyMyRangedBuilt = new LinkedList<Unit>();
    List<Unit> everyMyWorkersBuilt = new LinkedList<Unit>();
    List<Unit> everyMyHarvestersBuilt = new LinkedList<Unit>();
    List<Unit> everyMyHeaviesBuilt = new LinkedList<Unit>();
    List<Unit> everyMyLightsBuilt = new LinkedList<Unit>();
    
    public KamikazeBot(UnitTypeTable utt) {
        super(new AStarPathFinding());
        this.utt = utt;

        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
        barracksType = utt.getUnitType("Barracks");
        heavyType = utt.getUnitType("Heavy");
        lightType = utt.getUnitType("Light");
        rangedType = utt.getUnitType("Ranged");
    }
    

    @Override
    public void reset() {
    	super.reset();
    }
    
    

    
    @Override
    public AI clone() {
        return new KamikazeBot(utt);
    }
   
    
    @Override
    public PlayerAction getAction(int player, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
        
        
        // Check Size of Map at start to determine which strategy to employ.
        if (pgs.getWidth() * pgs.getHeight() <= 10 * 10)
        {
        	mapSmall = true;
        }
        if (pgs.getWidth() * pgs.getHeight() > 10 * 10 
        		&& pgs.getWidth() * pgs.getHeight() <=  16 * 16)
        {
        	mapMedium = true;
        }
        if (pgs.getWidth() * pgs.getHeight() > 16 * 16)
        {
        	mapLarge = true;
        }
        
     // Create a list of resources
        List<Unit> mapResources = new LinkedList<Unit>();
        
     // Create a list of my bases
        List<Unit> myBases = new LinkedList<Unit>();
     // Create a list of my barracks
        List<Unit> myBarracks = new LinkedList<Unit>();
      
     // Create a list of my workers
        List<Unit> myWorkers = new LinkedList<Unit>();
     // Create a list of my resource workers
        List<Unit> myHarvesters = new LinkedList<Unit>();
     // Create a list of my barracks workers
        List<Unit> myBarracksBuilders = new LinkedList<Unit>();
     // Create a list of my light units
        List<Unit> myLights = new LinkedList<Unit>();
     // Create a list of my heavy units
        List<Unit> myHeavies = new LinkedList<Unit>();
     // Get a list of my ranged units
        List<Unit> myRanged = new LinkedList<Unit>();
        
     // Create a list of enemy bases
        List<Unit> enemyBases = new LinkedList<Unit>();
     // Create a list of enemy barracks
        List<Unit> enemyBarracks = new LinkedList<Unit>();
      
     // Create a list of enemy workers
        List<Unit> enemyWorkers = new LinkedList<Unit>();
     // Create a list of enemy light units
        List<Unit> enemyLights = new LinkedList<Unit>();
     // Create a list of enemy heavy units
        List<Unit> enemyHeavies = new LinkedList<Unit>();
     // Get a list of enemy ranged units
        List<Unit> enemyRanged = new LinkedList<Unit>();
     // Get a list of all enemy units
        List<Unit> allEnemies = new LinkedList<Unit>();
        
        
        
        
        // Put all units in the game in their respective lists
        
        for (Unit r : pgs.getUnits()) 
        {
            if (r.getType().isResource && !mapResources.contains(r))
            {
            	mapResources.add(r);
            }
        }

        for (Unit u : pgs.getUnits()) 
        {
        	if (u.getPlayer() == player)
        	{
        		if (u.getType() == baseType)
        		{
        			myBases.add(u);
        		}
        		if (u.getType() == barracksType)
        		{
        			myBarracks.add(u);
        		}
        		if (u.getType() == workerType)
        		{
        			myWorkers.add(u);
        		}
        		if (u.getType() == workerType && !everyMyWorkersBuilt.contains(u))
        		{
        			everyMyWorkersBuilt.add(u);
        		}
        		if (u.getType() == lightType)
        		{
        			myLights.add(u);
        		}
        		if (u.getType() == lightType && !everyMyLightsBuilt.contains(u))
        		{
        			everyMyLightsBuilt.add(u);
        		}
        		if (u.getType() == heavyType)
        		{
        			myHeavies.add(u);
        		}
        		if (u.getType() == heavyType && !everyMyWorkersBuilt.contains(u))
        		{
        			everyMyHeaviesBuilt.add(u);
        		}
        		if (u.getType() == rangedType)
        		{
        			myRanged.add(u);
        		}
        		if (u.getType() == rangedType && !everyMyRangedBuilt.contains(u))
        		{
        			everyMyRangedBuilt.add(u);
        		}
        	}
        	if (u.getPlayer() != player && u.getPlayer() != -1)
        	{
        		allEnemies.add(u);
        		if (u.getType() == baseType)
        		{
        			enemyBases.add(u);
        		}
        		if (u.getType() == baseType)
        		{
        			enemyBases.add(u);
        		}
        		if (u.getType() == barracksType)
        		{
        			enemyBarracks.add(u);
        		}
        		if (u.getType() == workerType)
        		{
        			enemyWorkers.add(u);
        		}
        		if (u.getType() == lightType)
        		{
        			enemyLights.add(u);
        		}
        		if (u.getType() == heavyType)
        		{
        			enemyHeavies.add(u);
        		}
        		if (u.getType() == rangedType)
        		{
        			enemyRanged.add(u);
        		}
        	}
        }
        
        
        // Finds the position the player base spawns on the map
        if (!myBases.isEmpty())
        {
        	Unit base = myBases.get(0);
        	boolean topLeft = isBaseTopLeft(pgs, base);
        	boolean topRight = isBaseTopRight(pgs, base);
        	boolean bottomLeft = isBaseBottomLeft(pgs, base);
        	boolean bottomRight = isBaseBottomRight(pgs, base);
        	
        	// Changes where to build barracks based on where the player 
        	// base spawns.
        	if (topLeft){
            	buildOffsetX = 1;
            	buildOffsetY = 1;
            }
            
            if (topRight){
            	buildOffsetX = -1;
            	buildOffsetY = 1;
            }
            
            if (bottomLeft){
            	buildOffsetX = 1;
            	buildOffsetY = -1;
            }
            
            if (bottomRight){
            	buildOffsetX = -1;
            	buildOffsetY = -1;
            }
        }
        
        
        // Switch to worker rush if we have a lot of bases already
        if (myBases.size() > 3 && mapMedium)
        {
        	mapMedium = false;
        	workerRush = true;
        }
        

        
        
        //===================================================================================================================
	    // SMALL MAP STRATEGY
	    //===================================================================================================================
        
		if (mapSmall || workerRush)
		{
			if (mapResources.size() > 0)
			{
				// Creates a harvester for each base
				if (myHarvesters.size() <= 2  && !myWorkers.isEmpty())
				{
					for(int n = 0; n < myBases.size(); n++)
					{
						createHarvester(myWorkers, myHarvesters);
					}
				}
			}
			
			// Create more harvesters if a lot of resources present
			if (mapResources.size() > 7 && !myWorkers.isEmpty() && myHarvesters.size() < 5)
			{
				for(int n = 0; n < myWorkers.size(); n++)
				{
					createHarvester(myWorkers, myHarvesters);
				}
				
			}
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
								
					harvestClosestResource(u, myBases, mapResources);
					//System.out.print("Harvesting!");
				}
			}

			// Any workers will attack the nearest enemy Base first
			if (myWorkers.size() > 0)
	    	{
	    		for (Unit u: myWorkers)
				{
	    			// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					
					if (!myBases.isEmpty())
					{
						Unit base = myBases.get(0);
						// Rally in front of base
						moveToRallyPoint(u, pgs, gs, base, 0);
					}
					
					if (everyMyWorkersBuilt.size() > 4)
					{
		    			if(enemyBases.isEmpty())
		    			{
		    				attackNearestEnemy(u, p, gs);
		    			}
		    			else
		    			{
		    				attackNearestEnemy(u, p, gs);
		    				//attackNearestEnemyBase(u, p, gs, enemyBases);
		    			}
					}
				}
	    	}
			

	        if (myBases.size() > 0)
	        {
	        	for (Unit u: myBases)
	        	{
	        		if( p.getResources()>=workerType.cost)
	        		train(u, workerType);
	        	}
	        }
	        
	        
		}
		
		
		//===================================================================================================================
	    // MEDIUM MAP STRATEGY
	    //===================================================================================================================
		
		if (mapMedium)
		{
			// If there are a decent amount of resources left
			if(mapResources.size() > 0)
			{
				// Creates a harvester for each base
				if (myHarvesters.size() <= 1 + extraHarvesters && !myWorkers.isEmpty())
				{
					for(int n = 0; n < myBases.size() + extraHarvesters; n++)
					{
						createHarvester(myWorkers, myHarvesters);
					}
					
				}
			}
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
								
					harvestClosestResource(u, myBases, mapResources);
					//System.out.print("Harvesting!");
				}
			}
			
			// Assign a worker to build a barracks
			if (myBarracks.size() < 1 && myWorkers.size() > 0 && p.getResources() >= 5)
			{
				Unit newBarracksBuilder = myWorkers.remove(0);
				myBarracksBuilders.add(newBarracksBuilder);
			}
			
			// Build a barracks
			List<Integer> reservedPositions = new LinkedList<Integer>();
			if (myBarracksBuilders.size() > 0 && p.getResources() >= 5)
			{
				for (Unit u: myBarracksBuilders)
				{
					if (!myBases.isEmpty())
					{
						// Build near base
						Unit initialBase = myBases.get(0);
						buildIfNotAlreadyBuilding(u, barracksType, initialBase.getX()+buildOffsetX, initialBase.getY()+buildOffsetY, reservedPositions,p,pgs);
					}
					else
					{
						// Build where builder is
						buildIfNotAlreadyBuilding(u, barracksType, u.getX(), u.getY(), reservedPositions,p,pgs);
					}
				}
			}
			
			
			// Any extra workers will attack the nearest enemy
			if (myWorkers.size() > 0)
	    	{
	    		for (Unit u: myWorkers)
				{
					attackNearestEnemy(u, p, gs);
				}
	    	}
			
			// Any extra heavies will attack the nearest enemy
			if (myHeavies.size() > 0)
			{
				for (Unit u: myHeavies)
				{
					// Attack anything that comes in range
					attackEnemyInBowRange(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (everyMyHeaviesBuilt.size() > 0)
					{
						attackNearestEnemy(u, p, gs);
									
					}
				}
			}
			
			// Any extra lights will attack the nearest enemy
			if (myLights.size() > 0)
			{
				for (Unit u: myLights)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					
					if (!myBases.isEmpty())
					{
						Unit base = myBases.get(0);
						// Rally in front of base
						moveToRallyPoint(u, pgs, gs, base, 3);
					}
					// After a number of ranged units are built, attack
					if (everyMyLightsBuilt.size() > 0)
					{
						attackNearestEnemy(u, p, gs);
												
					}
				}
			}
			
			// Any extra ranged units will attack the nearest enemy
			if (myRanged.size() > 0)
			{
				for (Unit u: myRanged)
				{
					// Attack anything that comes in range
					attackEnemyInBowRange(u, p, gs, pgs, allEnemies);
					
					if (!myBases.isEmpty())
					{
						Unit base = myBases.get(0);
						// Rally in front of base
						moveToRallyPoint(u, pgs, gs, base, 1);
					}
					// After a number of ranged units are built, attack
					if (everyMyRangedBuilt.size() > 3)
					{
						attackNearestEnemy(u, p, gs);
					}
				}
			}
			
			// Train workers unless there is already a spare
	        if (myBases.size() > 0 && myWorkers.size() < maxAttackWorkers)
	        {
	        	for (Unit u: myBases)
	        	{
	        		if( p.getResources()>=workerType.cost)
	        		train(u, workerType);
	        	}
	        }
	        
	        // Train a ranged when possible
	        if (myBarracks.size() > 0 && p.getResources() > 2)
	        {
	        	for (Unit u: myBarracks)
	        	{
	        		if (myLights.size() <= myRanged.size())
	        		{
	        			train(u, lightType);
	        		}
	        		else
	        		{
	        			train(u, rangedType);
	        		}
	        		
	        	}
	        }
		}
		
		//===================================================================================================================
	    // LARGE MAP STRATEGY
	    //===================================================================================================================
		
		if (mapLarge)
		{
			if (mapResources.size() > 0)
			{
				// Creates a harvester for each base
				if (myHarvesters.size() <= 1 + extraHarvesters && !myWorkers.isEmpty())
				{
					for(int n = 0; n < myBases.size() + extraHarvesters; n++)
					{
						createHarvester(myWorkers, myHarvesters);
					}
					
				}
			}
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					
					harvestClosestResource(u, myBases, mapResources);
					//System.out.print("Harvesting!");
				}
			}
			
			// Assign a worker to build a barracks
			if (myBarracks.size() == 0 && myWorkers.size() > 0 && p.getResources() >= 5)
			{
				Unit newBarracksBuilder = myWorkers.remove(0);
				myBarracksBuilders.add(newBarracksBuilder);
			}
			if (myBarracks.size() == 1 && myWorkers.size() > 0 && p.getResources() >= 7)
			{
				Unit newBarracksBuilder = myWorkers.remove(0);
				myBarracksBuilders.add(newBarracksBuilder);
			}
			
			// Build a barracks
			List<Integer> reservedPositions = new LinkedList<Integer>();
			if (myBarracksBuilders.size() > 0 && p.getResources() >= 5)
			{
				for (Unit u: myBarracksBuilders)
				{
					if (!myBases.isEmpty())
					{
						Unit initialBase = myBases.get(0);
						buildIfNotAlreadyBuilding(u, barracksType, initialBase.getX()+buildOffsetX, initialBase.getY()+buildOffsetY, reservedPositions,p,pgs);
					}
					else
					{
						buildIfNotAlreadyBuilding(u, barracksType, u.getX(), u.getY(), reservedPositions,p,pgs);
					}
					
				}
			}
			
			
			// Any extra workers will attack the nearest enemy
			if (myWorkers.size() > 0)
	    	{
				for (Unit u: myWorkers)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (myWorkers.size() > 0)
					{
						attackNearestEnemy(u, p, gs);
						
					}
				}
	    	}
			
			// Any extra heavies will attack the nearest enemy
			if (myHeavies.size() > 0)
	    	{
				for (Unit u: myHeavies)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (everyMyHeaviesBuilt.size() > 0)
					{
						attackNearestEnemy(u, p, gs);
						
					}
				}
	    	}
			
			// Any extra lights will attack the nearest enemy
			if (myLights.size() > 0)
			{
				for (Unit u: myLights)
				{
					// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					
					if (!myBases.isEmpty())
					{
						Unit base = myBases.get(0);
						// Rally in front of base
						moveToRallyPoint(u, pgs, gs, base, 3);
					}
					// After a number of ranged units are built, attack
					if (everyMyLightsBuilt.size() > 3)
					{
						attackNearestEnemy(u, p, gs);
									
					}
				}
			}
			
			// Any extra ranged units will attack the nearest enemy
			if (myRanged.size() > 0)
			{
				for (Unit u: myRanged)
				{
					// Attack anything that comes in range
					attackEnemyInBowRange(u, p, gs, pgs, allEnemies);

					if (!myBases.isEmpty())
					{
						Unit base = myBases.get(0);
						// Rally in front of base
						moveToRallyPoint(u, pgs, gs, base, 1);
					}
					// After a number of ranged units are built, attack
					if (everyMyRangedBuilt.size() > 3)
					{
						attackNearestEnemy(u, p, gs);
						
					}
				}
			}
			
			// Train workers unless there is already a spare
	        if (myBases.size() > 0 && myWorkers.size() < maxAttackWorkers)
	        {
	        	for (Unit u: myBases)
	        	{
	        		if( p.getResources()>=workerType.cost)
	        		train(u, workerType);
	        	}
	        }
	        
	        // Train a ranged unit when possible
	        if (myBarracks.size() > 0 && p.getResources() > 2)
	        {
	        	for (Unit u: myBarracks)
	        	{
	        		// Trains until 3 light and ranged, then trains heavy
	        		if (myLights.size() <= myRanged.size() && myRanged.size() < 4)
	        		{
	        			train(u, lightType);
	        		}
	        		else if (myLights.size() > myRanged.size() && myRanged.size() < 4)
	        		{
	        			train(u, rangedType);
	        		}
	        		else
	        		{
	        			train(u, heavyType);
	        		}
	        	}
	        }
	        
	        
		}
		
        
        return translateActions(player, gs);
    }
    
    //===================================================================================================================
    // Functions
    //===================================================================================================================
    
    /**
     * Takes a worker and turns it into a harvester.
     */
    public void createHarvester(List<Unit> myWorkers, List<Unit> myHarvesters)
    {
    	myHarvesters.add(myWorkers.get(0));
	    myWorkers.remove(0);   
    }
    
   
    /**
     * Determines if the Players Base is in the Top Left
     * of the map.
     */
    public boolean isBaseTopLeft(PhysicalGameState pgs, Unit base) 
    {
        int x = base.getX() - (pgs.getWidth()/2); // x>0=right, x<0=left
        int y = base.getY() - (pgs.getHeight()/2); //y>0=down, y<0=up

        if (x < 0 && y <= 0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }
    
    /**
     * Determines if the Players Base is in the Top Right
     * of the map.
     */
    public boolean isBaseTopRight(PhysicalGameState pgs, Unit base) 
    {
        int x = base.getX() - (pgs.getWidth()/2); // x>0=right, x<0=left
        int y = base.getY() - (pgs.getHeight()/2); //y>0=down, y<0=up

        if (x >=0 && y <=0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }
    
    /**
     * Determines if the Players Base is in the Bottom Left
     * of the map.
     */
    public boolean isBaseBottomLeft(PhysicalGameState pgs, Unit base) 
    {
        int x = base.getX() - (pgs.getWidth()/2); // x>0=right, x<0=left
        int y = base.getY() - (pgs.getHeight()/2); //y>0=down, y<0=up

        if (x < 0 && y > 0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }
    
    /**
     * Determines if the Players Base is in the Bottom Right
     * of the map.
     */
    public boolean isBaseBottomRight(PhysicalGameState pgs, Unit base) 
    {
        int x = base.getX() - (pgs.getWidth()/2); // x>0=right, x<0=left
        int y = base.getY() - (pgs.getHeight()/2); //y>0=down, y<0=up

        if (x >=0 && y > 0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }
    
    /**
     * Creates a set of rally points for the units to move 
     * to based of the position of the Player Base on the map. 
     */
    public void moveToRallyPoint(Unit unit, PhysicalGameState pgs, GameState gs, Unit base, int rallyPointOffset)
    {
    	
    	for (int n = 0; n < 6; n++)  
    	{
    		boolean topLeft = isBaseTopLeft(pgs, base);
        	if (topLeft) 
        	{
        		boolean checkPath = pf.pathExists(unit, ((0 + (pgs.getWidth()/5)+rallyPointOffset+n) + (pgs.getHeight()/2-n) * pgs.getWidth()), gs, null);
        		if (checkPath) 
        		{
        			move(unit, (0+rallyPointOffset+(pgs.getWidth()/5)+n), (pgs.getHeight()/2-n));
        			break;
        		}
        	}
        	
        	boolean topRight = isBaseTopRight(pgs, base);
        	if (topRight) 
        	{
        		boolean checkPath = pf.pathExists(unit, ((pgs.getWidth()-(pgs.getWidth()/5)-rallyPointOffset-n) + (pgs.getHeight()/2-n) * pgs.getWidth()), gs, null);
        		if (checkPath) 
        		{
        			move(unit, (pgs.getWidth()-(pgs.getWidth()/5)-rallyPointOffset-n), (pgs.getHeight()/2-n));
        			break;
        		}
        	}
        	
        	boolean bottomRight = isBaseBottomRight(pgs, base);
        	if (bottomRight) 
        	{
        		boolean checkPath = pf.pathExists(unit, ((pgs.getWidth()-(pgs.getWidth()/5)-rallyPointOffset-n)+(pgs.getHeight()/2+n)*pgs.getWidth()), gs, null);
        		if (checkPath) 
        		{
        			move(unit, ((pgs.getWidth())-(pgs.getWidth()/5)-rallyPointOffset-n), pgs.getHeight()/2+n);
        			break;
        		}
        	}
        	
        	boolean bottomLeft = isBaseBottomLeft(pgs, base);
        	if (bottomLeft) 
        	{
        		boolean checkPath = pf.pathExists(unit, ((0 + (pgs.getWidth()/5)+rallyPointOffset+n) + (pgs.getHeight()/2+n) * pgs.getWidth()), gs, null);
        		if (checkPath) 
        		{
        			move(unit, (0+rallyPointOffset+(pgs.getWidth()/5)+n), (pgs.getHeight()/2+n));
        			break;
        		}
        	}
    	}	
    }
    
    /**
     * Sets unit to harvest the nearest resource and take it back
     * to it's nearest base.
     */
    public void harvestClosestResource(Unit harvestWorker, List<Unit> myBases, List<Unit> resources)
    {
    	Unit closestBase = null;
    	Unit closestResource = null;
        int closestDistance = 0;
        if (myBases.size() > 0)
        {
        	closestBase = myBases.get(0);
        	
        }
        for(Unit r:resources) 
        {
            if (r.getType().isResource) 
            { 
                int d = Math.abs(r.getX() - harvestWorker.getX()) + Math.abs(r.getY() - harvestWorker.getY());
                if (closestResource==null || d<closestDistance) 
                {
                    closestResource = r;
                    closestDistance = d;
                }
                if (closestResource != null && closestBase != null)
                {
                	harvest(harvestWorker, closestResource, closestBase);
                }
            }
        } 
    }
    
    /**
     * Sets unit to attack the nearest enemy.
     */
    public void attackNearestEnemy(Unit u, Player p, GameState gs) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestEnemy = null;
        int closestDistance = 0;
        for(Unit u2:pgs.getUnits()) {
            if (u2.getPlayer()>=0 && u2.getPlayer()!=p.getID()) { 
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestEnemy==null || d<closestDistance) {
                    closestEnemy = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy!=null) {
            attack(u,closestEnemy);
        }
    }
    
    /**
     * Sets unit to attack the nearest enemy Base.
     */
    public void attackNearestEnemyBase(Unit u, Player p, GameState gs, List<Unit> enemyBases) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestEnemy = null;
        int closestDistance = 0;
        for(Unit u2: enemyBases) {
            if (u2.getPlayer()>=0 && u2.getPlayer()!=p.getID()) { 
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestEnemy==null || d<closestDistance) {
                    closestEnemy = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy!=null) {
            attack(u,closestEnemy);
        }
    }
    
    /**
     * Tells unit to attack anything that comes within 3 spaces
     * of itself.
     */
    public void attackEnemyInBowRange(Unit u, Player p, GameState gs, PhysicalGameState pgs, List<Unit> allEnemies) 
    {
        int uXPos = u.getX();
        int uYPos = u.getY();
        
        List<Unit> closeUnits = new LinkedList<Unit>();
        for(Unit u2: allEnemies) {
        	int range = 7;
			int x = uXPos-3;
			int y = uYPos-3;
			if((Math.abs(u2.getX() - x )<=range  &&  Math.abs(u2.getY() - y)<=range)){
        		closeUnits.add(u2);
        	}
        }
        
        Unit closestEnemy = null;
        int closestDistance = 0;
        for(Unit u3: closeUnits) {
            if (u3.getPlayer()>=0 && u3.getPlayer()!=p.getID()) { 
                int d = Math.abs(u3.getX() - u.getX()) + Math.abs(u3.getY() - u.getY());
                if (closestEnemy==null || d<closestDistance) {
                    closestEnemy = u3;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy!=null) {
            attack(u,closestEnemy);
        }
    }
    
    /**
     * Tells unit to attack anything that comes within 1 space
     * of itself.
     */
    public void attackEnemyInRangeMelee(Unit u, Player p, GameState gs, PhysicalGameState pgs, List<Unit> allEnemies) 
	{
        int uXPos = u.getX();
        int uYPos = u.getY();
        
        List<Unit> closeUnits = new LinkedList<Unit>();
        for(Unit u2: allEnemies) {
        	int range = 3;
			int x = uXPos-3;
			int y = uYPos-3;
			if((Math.abs(u2.getX() - x )<=range  &&  Math.abs(u2.getY() - y)<=range)){
        		closeUnits.add(u2);
        	}
        }
        
        Unit closestEnemy = null;
        int closestDistance = 0;
        for(Unit u3: closeUnits) {
            if (u3.getPlayer()>=0 && u3.getPlayer()!=p.getID()) { 
                int d = Math.abs(u3.getX() - u.getX()) + Math.abs(u3.getY() - u.getY());
                if (closestEnemy==null || d<closestDistance) {
                    closestEnemy = u3;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy!=null) {
            attack(u,closestEnemy);
        }
    }
    
    
    
    
    
    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
}
