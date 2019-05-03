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
    
    boolean workerRush = false;
    boolean standardStrat = false;
    boolean mapSmall = false;
    boolean mapMedium = false;
    boolean mapLarge = false;
    boolean hasHarvester = false;
    boolean armyAttack = false;
    
    List<Unit> numRangedBuilt = new LinkedList<Unit>();
    List<Unit> numWorkersBuilt = new LinkedList<Unit>();
    List<Unit> numHarvestersBuilt = new LinkedList<Unit>();
    List<Unit> numHeaviesBuilt = new LinkedList<Unit>();
    
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
        
        
        // Check Size of Map
        if (pgs.getWidth() * pgs.getHeight() <= 100)
        {
        	mapSmall = true;
        }
        if (pgs.getWidth() * pgs.getHeight() > 100 
        		&& pgs.getWidth() * pgs.getHeight() <= 144)
        {
        	mapMedium = true;
        }
        if (pgs.getWidth() * pgs.getHeight() > 144)
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
     // Create a list of my attack workers
        List<Unit> myAttackWorkers = new LinkedList<Unit>();
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
        
        
        
        
        /* -----------Put all units in the game in their respective lists----------------------*/
        
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
        		if (u.getType() == workerType && !numWorkersBuilt.contains(u))
        		{
        			numWorkersBuilt.add(u);
        		}
        		if (u.getType() == lightType)
        		{
        			myLights.add(u);
        		}
        		if (u.getType() == heavyType)
        		{
        			myHeavies.add(u);
        		}
        		if (u.getType() == heavyType && !numWorkersBuilt.contains(u))
        		{
        			numHeaviesBuilt.add(u);
        		}
        		if (u.getType() == rangedType)
        		{
        			myRanged.add(u);
        		}
        		if (u.getType() == rangedType && !numRangedBuilt.contains(u))
        		{
        			numRangedBuilt.add(u);
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
        
        /*--------------------What to do if on a small map----------------------*/
        
		if (mapSmall)
		{
			// Choose a worker to harvest resources
			if (myHarvesters.size() == 0 && numWorkersBuilt.size() > 4 && p.getResources() < 1)
			{
				if(myWorkers.size() > 0)
				{
					Unit newHarvester = myWorkers.get(0);
					myHarvesters.add(newHarvester);
					numHarvestersBuilt.add(newHarvester);
				}
				
			}
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					harvestClosestResource(u, myBases, mapResources);
					System.out.print("Harvesting!");
				}
			}

			// Any workers will attack the nearest enemy Base first
			if (myWorkers.size() > 0)
	    	{
	    		for (Unit u: myWorkers)
				{
	    			// Attack anything that comes in range
					attackEnemyInRangeMelee(u, p, gs, pgs, allEnemies);
					if (numWorkersBuilt.size() > 3)
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
			

	        if (myBases.size() > 0 && myWorkers.size() < 10)
	        {
	        	for (Unit u: myBases)
	        	{
	        		if( p.getResources()>=workerType.cost)
	        		train(u, workerType);
	        	}
	        }
	        
	        if (myBarracks.size() > 0 && p.getResources() > 2)
	        {
	        	for (Unit u: myBarracks)
	        	{
	        		train(u, heavyType);
	        	}
	        }
		}
		
		
		/*--------------------What to do if on a medium sized map----------------------*/
		
		if (mapMedium)
		{
			// Choose 2 workers to harvest resources
			if (myHarvesters.size() == 0 && myWorkers.size() > 0)
			{
				Unit newHarvester = myWorkers.remove(0);
		        myHarvesters.add(newHarvester);
		        numHarvestersBuilt.add(newHarvester);
			}
			if (myHarvesters.size() == 1 && myWorkers.size() > 0)
			{
				Unit newHarvester = myWorkers.remove(0);
		        myHarvesters.add(newHarvester);
		        numHarvestersBuilt.add(newHarvester);
			}
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					harvestClosestResource(u, myBases, mapResources);
					//System.out.print("Harvesting!");
				}
			}
			
			// Assign a worker to build a barracks
			if (myBarracks.size() < 1 && myWorkers.size() > 0 && p.getResources() >= 6)
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
						buildIfNotAlreadyBuilding(u, barracksType, initialBase.getX()+2, initialBase.getY()+2, reservedPositions,p,pgs);
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
					attackEnemyInRange(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (numHeaviesBuilt.size() > 0)
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
					attackEnemyInRange(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (numRangedBuilt.size() > 2)
					{
						attackNearestEnemy(u, p, gs);
					}
				}
			}
			
			// Train workers unless there is already a spare
	        if (myBases.size() > 0 && myWorkers.size() < 1)
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
	        		if (myRanged.size() > 2)
	        		{
	        			train(u, heavyType);
	        		}
	        		else
	        		{
	        			train(u, rangedType);
	        		}
	        	}
	        }
		}
		
		/*--------------------What to do if on a larger map----------------------*/
		
		if (mapLarge)
		{
			// Choose 3 workers to harvest resources
			if (myHarvesters.size() == 0 && myWorkers.size() > 0)
			{
				Unit newHarvester = myWorkers.remove(0);
		        myHarvesters.add(newHarvester);
		        if (!numWorkersBuilt.contains(newHarvester)) {
		        	numHarvestersBuilt.add(newHarvester);
		        }
		        
			}
			if (myHarvesters.size() == 1 && myWorkers.size() > 0)
			{
				Unit newHarvester = myWorkers.remove(0);
		        myHarvesters.add(newHarvester);
		        if (!numWorkersBuilt.contains(newHarvester)) {
		        	numHarvestersBuilt.add(newHarvester);
		        }
			}
			if (myHarvesters.size() == 2 && myWorkers.size() > 0)
			{
				Unit newHarvester = myWorkers.remove(0);
		        myHarvesters.add(newHarvester);
		        if (!numWorkersBuilt.contains(newHarvester)) {
		        	numHarvestersBuilt.add(newHarvester);
		        }
			}
			
			
			// Harvest resource
			if (myHarvesters.size() > 0)
			{
				for (Unit u: myHarvesters)
				{
					harvestClosestResource(u, myBases, mapResources);
					//System.out.print("Harvesting!");
				}
			}
			
			// Assign a worker to build a barracks
			if (myBarracks.size() == 0 && myWorkers.size() > 0 && p.getResources() >= 6)
			{
				Unit newBarracksBuilder = myWorkers.remove(0);
				myBarracksBuilders.add(newBarracksBuilder);
			}
			if (myBarracks.size() == 1 && myWorkers.size() > 0 && p.getResources() >= 10)
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
						buildIfNotAlreadyBuilding(u, barracksType, initialBase.getX()+2, initialBase.getY()+2, reservedPositions,p,pgs);
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
					attackEnemyInRange(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (numHeaviesBuilt.size() > 1)
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
					attackEnemyInRange(u, p, gs, pgs, allEnemies);
					// After a number of ranged units are built, attack
					if (numRangedBuilt.size() > 2)
					{
						attackNearestEnemy(u, p, gs);
						
					}
				}
			}
			
			// Train workers unless there is already a spare
	        if (myBases.size() > 0 && myWorkers.size() < 1 && myBarracks.size() < 2)
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
	        		if (myRanged.size() > 3)
	        		{
	        			train(u, heavyType);
	        		}
	        		else
	        		{
	        			train(u, rangedType);
	        		}
	        	}
	        }
	        //System.out.print(numRangedBuilt);
	        
		}
		
        
        return translateActions(player, gs);
    }
        
    
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
    
    public void attackEnemyInRange(Unit u, Player p, GameState gs, PhysicalGameState pgs, List<Unit> allEnemies) {
        
        
        int uXPos = u.getX();
        int uYPos = u.getY();
        
        //pgs.getUnitsAround(uXPos-3, uYPos-3, 25);
        
        List<Unit> closeUnits = new LinkedList<Unit>();
        for(Unit u2: allEnemies) {
        	int squareRange = 7;
			int x = uXPos-3;
			int y = uYPos-3;
			if((Math.abs(u2.getX() - x )<=squareRange  &&  Math.abs(u2.getY() - y)<=squareRange)){
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
    
public void attackEnemyInRangeMelee(Unit u, Player p, GameState gs, PhysicalGameState pgs, List<Unit> allEnemies) {
        
        
        int uXPos = u.getX();
        int uYPos = u.getY();
        
        //pgs.getUnitsAround(uXPos-3, uYPos-3, 25);
        
        List<Unit> closeUnits = new LinkedList<Unit>();
        for(Unit u2: allEnemies) {
        	int squareRange = 3;
			int x = uXPos-3;
			int y = uYPos-3;
			if((Math.abs(u2.getX() - x )<=squareRange  &&  Math.abs(u2.getY() - y)<=squareRange)){
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
    
    /*
    // Barracks behaviour
   // public void barracksBehaviour(Unit u, Player p, PhysicalGameState pgs) {
   //     if (p.getResources() >= heavyType.cost) {
   //         train(u, heavyType);
   //     }
   // }
    
    public void barracksBehaviour(List<Unit> myBarracks, Player p, GameState gs, PhysicalGameState pgs)
    {
    	int nMyBarracks = myBarracks.size();
    	for (Unit u : myBarracks)
    		if (p.getResources() >= heavyType.cost) {
    			train(u, heavyType);
        }
    }
    

    
    // Trains worker if it can
    public void baseBehaviour(Unit u,Player p, PhysicalGameState pgs) {
        int nMyWorkers = 0;
        if (p.getResources()>=workerType.cost) {
        	train(u, workerType);	
        }
    }
    
    
    public void meleeUnitBehaviour(Unit u, Player p, GameState gs) {
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
    
    
    
    
    public void workerBehaviour(List<Unit> myWorkers, Player p, GameState gs, PhysicalGameState pgs)
    {
    	//PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyWorkers = myWorkers.size();
    	int nBases = 0;
    	int nBarracks = 0;
    	int nGatherers = 0;
    	int resourcesUsed = 0;
        Unit harvestWorker = null;
    	
    	List<Unit> freeWorkers = new LinkedList<Unit>();
    	List<Unit> attackWorkers = new LinkedList<Unit>();
    	
        freeWorkers.addAll(myWorkers);
    	
		if (myWorkers.isEmpty()) return;
		
		// Check how many bases and barracks there are
		for(Unit u2:pgs.getUnits()) {
            if (u2.getType() == baseType && 
                u2.getPlayer() == p.getID()) nBases++;
            
            if (u2.getType() == barracksType && 
                    u2.getPlayer() == p.getID()) nBarracks++;
        }
		

		
		        
		
		List<Integer> reservedPositions = new LinkedList<Integer>();
		// Builds a base if there is none
        if (nBases==0 && !freeWorkers.isEmpty()) {
            // build a base:
            if (p.getResources()>=baseType.cost + resourcesUsed) {
                Unit u = freeWorkers.remove(0);
                buildIfNotAlreadyBuilding(u,baseType,u.getX(),u.getY(),reservedPositions,p,pgs);
                resourcesUsed+=baseType.cost;
            }
        }
        
     // Builds a barracks if there is none
        if (nBarracks==0 && !freeWorkers.isEmpty()) {
            // build a barracks:
        	
            if (p.getResources()>=barracksType.cost + resourcesUsed) {
            	
                Unit u = freeWorkers.remove(0);
                buildIfNotAlreadyBuilding(u,barracksType,u.getX(),u.getY(),reservedPositions,p,pgs);
                System.out.print("Hello");
                resourcesUsed+=barracksType.cost;
            }
        }
        
        
        // Create a harvest worker if there are more than 5 free workers
        if (freeWorkers.size()>1) harvestWorker = freeWorkers.remove(0);
        
        
        
        // Harvest from the nearest resource
        if (harvestWorker!=null) {
            Unit closestBase = null;
            Unit closestResource = null;
            int closestDistance = 0;
            for(Unit u2:pgs.getUnits()) {
                if (u2.getType().isResource) { 
                    int d = Math.abs(u2.getX() - harvestWorker.getX()) + Math.abs(u2.getY() - harvestWorker.getY());
                    if (closestResource==null || d<closestDistance) {
                        closestResource = u2;
                        closestDistance = d;
                    }
                }
            }
            closestDistance = 0;
            for(Unit u2:pgs.getUnits()) {
                if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) { 
                    int d = Math.abs(u2.getX() - harvestWorker.getX()) + Math.abs(u2.getY() - harvestWorker.getY());
                    if (closestBase==null || d<closestDistance) {
                        closestBase = u2;
                        closestDistance = d;
                    }
                }
            }
            if (closestResource!=null && closestBase!=null) {
                AbstractAction aa = getAbstractAction(harvestWorker);
                if (aa instanceof Harvest) {
                    Harvest h_aa = (Harvest)aa;
                    if (h_aa.getTarget() != closestResource || h_aa.getBase()!=closestBase) harvest(harvestWorker, closestResource, closestBase);
                } else {
                    harvest(harvestWorker, closestResource, closestBase);
                }
            }
        }
        
        
        // Set the rest of the workers to attack
        for(Unit u:freeWorkers) attackNearestEnemy(u, p, gs);
	
    }
    
    */
    
    
    
    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
}
