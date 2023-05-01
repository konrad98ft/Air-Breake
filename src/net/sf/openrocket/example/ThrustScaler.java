package net.sf.openrocket.example;

import net.sf.openrocket.aerodynamics.AerodynamicCalculator;
import net.sf.openrocket.aerodynamics.AerodynamicForces;
import net.sf.openrocket.aerodynamics.FlightConditions;
import net.sf.openrocket.rocketcomponent.FlightConfiguration;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.simulation.FlightDataBranch;
import net.sf.openrocket.simulation.FlightDataType;
import net.sf.openrocket.simulation.SimulationConditions;
import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.simulation.extension.AbstractSimulationExtension;
import net.sf.openrocket.simulation.listeners.AbstractSimulationListener;
import net.sf.openrocket.unit.UnitGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThrustScaler extends AbstractSimulationExtension {
	
	// Save it as a FlightDataType
	private static final FlightDataType predApg = FlightDataType.getType("X_Predicted Apogee", "predApg", UnitGroup.UNITS_COEFFICIENT);
	private static final ArrayList<FlightDataType> types = new ArrayList<FlightDataType>();
	
	ThrustScaler() 
	{
		types.add(predApg);
	}
	
	
	@Override
	public String getName() {
		return "Thrust scaling \u00D7" + getMultiplier();
	}
	
	@Override
	public String getDescription() {
		// This description is shown when the user clicks the info-button on the extension
		return "This extension multiplies the thrust of motors by a user-defined multiplier.";
	}
	
	@Override
	public void initialize(SimulationConditions conditions) throws SimulationException {
		conditions.getSimulationListenerList().add(new ThrustScalerSimulationListener(getMultiplier()));
	}
	
	public double getMultiplier() {
		return config.getDouble("multiplier", 1.0);
	}
	
	public void setMultiplier(double multiplier) {
		config.put("multiplier", multiplier);
		fireChangeEvent();
	}
	
	public class ThrustScalerSimulationListener extends AbstractSimulationListener 
	{
		private double multiplier;
		
		public ThrustScalerSimulationListener(double multiplier) 
		{
			super();
			this.multiplier = multiplier;
		}
		
		@Override
		public double postSimpleThrustCalculation(SimulationStatus status, double thrust) throws SimulationException 
		{
			return thrust * multiplier;
		}

		@Override
		public FlightConditions postFlightConditions(SimulationStatus status, FlightConditions flightConditions) throws SimulationException 
		{
			
			//status.getFlightData().setValue(cdm, aerodynamicPart + propulsivePart);
			status.getFlightData().setValue(predApg, 900);
			
			return flightConditions;
		}
		
	}
	


}
