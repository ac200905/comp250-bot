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
import java.util.List;
import rts.*;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class KamikazeBot extends AbstractionLayerAI {    
    private UnitTypeTable utt;
    private UnitType workerType;
    UnitType baseType;
    boolean workerRush = false;
    
    public KamikazeBot(UnitTypeTable utt) {
        super(new AStarPathFinding());
        this.utt = utt;
        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
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
        	
        
        
        		
            // TODO: issue commands to units
        
     // behavior of bases:
        for(Unit u:pgs.getUnits()) {
            if (u.getType()==baseType && 
                u.getPlayer() == player && 
                gs.getActionAssignment(u)==null) {
                baseBehavior(u,p,pgs);
            }
        }
        
     // behavior of melee units:
        for(Unit u:pgs.getUnits()) {
            if (u.getType().canAttack && u.getType().canHarvest && 
                u.getPlayer() == player && 
                gs.getActionAssignment(u)==null) {
                meleeUnitBehavior(u,p,gs);
            }        
        }
        
        return translateActions(player, gs);
    }
    
    public void baseBehavior(Unit u,Player p, PhysicalGameState pgs) {
        if (p.getResources()>=workerType.cost) train(u, workerType);
    }
    
    public void meleeUnitBehavior(Unit u, Player p, GameState gs) {
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
    
    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
}
