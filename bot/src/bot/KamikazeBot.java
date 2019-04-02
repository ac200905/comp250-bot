package bot;

import ai.abstraction.AbstractionLayerAI;
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
    private UnitTypeTable utt;
    private UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType heavyType;
    UnitType lightType;
    UnitType rangedType;
    
    boolean workerRush = false;
    boolean standardStrat = false;
    
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
        if (pgs.getWidth() * pgs.getHeight() <= 64) {
        	workerRush = true;
        }
        else
        {
        	standardStrat = true;
        }
        
        List<Unit> myBases = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == baseType
                    && u.getPlayer() == player) {
                myBases.add(u);
            }
        }
        baseBehaviour(myBases, p, gs);
        
        List<Unit> myBarracks = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == barracksType
                    && u.getPlayer() == player) {
                myBarracks.add(u);
            }
        }
        barracksBehaviour(myBarracks, p, gs);
        	
        List<Unit> myWorkers = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType().canHarvest
                    && u.getPlayer() == player) {
                myWorkers.add(u);
            }
        }
        workerBehaviour(myWorkers, p, gs);
        
        List<Unit> myLights = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == lightType 
                    && u.getPlayer() == player) {
                myLights.add(u);
            }
        }
        lightBehaviour(myLights, p, gs);
        
        List<Unit> myHeavies = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == heavyType 
                    && u.getPlayer() == player) {
                myHeavies.add(u);
            }
        }
        heavyBehaviour(myHeavies, p, gs);
        
        List<Unit> myRanged = new LinkedList<Unit>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == rangedType 
                    && u.getPlayer() == player) {
                myRanged.add(u);
            }
        }
        rangedBehaviour(myRanged, p, gs);

        
        		
            // TODO: issue commands to units
        
        if (workerRush)
        {
        	// behaviour of bases:
            for(Unit u:pgs.getUnits()) {
                if (u.getType()==baseType && 
                    u.getPlayer() == player && 
                    gs.getActionAssignment(u)==null) {
                    baseBehaviour(u,p,pgs);
                }
            }
            
            // behaviour of melee units
            for(Unit u:pgs.getUnits()) {
                if (u.getType().canAttack && u.getType().canHarvest && 
                    u.getPlayer() == player && 
                    gs.getActionAssignment(u)==null) {
                    meleeUnitBehaviour(u,p,gs);
                }        
            }
            
            return translateActions(player, gs);
        }
        
        if (standardStrat)
        {
        	// behaviour of bases:
            for(Unit u:pgs.getUnits()) {
                if (u.getType()==baseType && 
                    u.getPlayer() == player && 
                    gs.getActionAssignment(u)==null) {
                    baseBehaviour(u,p,pgs);
                }
            }
            
            // behaviour of melee units
            for(Unit u:pgs.getUnits()) {
                if (u.getType().canAttack && u.getType().canHarvest && 
                    u.getPlayer() == player && 
                    gs.getActionAssignment(u)==null) {
                    meleeUnitBehaviour(u,p,gs);
                }        
            }
            
            return translateActions(player, gs);
        }
		return null;
    }
    
    public void baseBehaviour(Unit u,Player p, PhysicalGameState pgs) {
        if (p.getResources()>workerType.cost) train(u, workerType);
        if (p.getResources()==workerType.cost) {
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
    
    public void workerBehaviour(List<Unit> myWorkers, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyWorkers = myWorkers.size();
    	int nBases = 0;
    	int nGatherers = 0;
    	
    	if (nMyWorkers <= 4)
    	{
    		
    	}
    	
    	//number of workers
    	//if number of workers created above 4, assign resource gatherer
    }
    
    public void lightBehaviour(List<Unit> myLights, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyLights = myLights.size();
    	
    }
    
    public void heavyBehaviour(List<Unit> myHeavies, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyHeavies = myHeavies.size();
    	
    }
    
    public void rangedBehaviour(List<Unit> myRanged, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyRanged = myRanged.size();
    }
    
    public void barracksBehaviour(List<Unit> myBarracks, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyBarracks = myBarracks.size();
    }
    
    public void baseBehaviour(List<Unit> myBases, Player p, GameState gs)
    {
    	PhysicalGameState pgs = gs.getPhysicalGameState();
    	int nMyBases = myBases.size();
    }
    
    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
}
