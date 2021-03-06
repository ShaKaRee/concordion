package org.concordion.internal.command;

import java.util.ArrayList;
import java.util.List;

import org.concordion.api.*;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.concordion.internal.FailFastException;
import org.concordion.internal.SpecificationDescriber;

public class SpecificationCommand extends AbstractCommand {

    @Override
    public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        throw new IllegalStateException("Unexpected call to " + getClass().getSimpleName() + "'s setUp() method. Only the execute() method should be called.");
    }
    
    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        if (specificationDescriber != null) {
            resultRecorder.setSpecificationDescription(specificationDescriber.getDescription(commandCall.getResource()));
        }

        try {
            commandCall.getChildren().processSequentially(evaluator, resultRecorder);
        } catch (FailFastException e) {
            // Ignore - it'll be re-thrown later if necessary.
        }
    }

    public void start(CommandCall commandCall) {
        announceBeforeProcessingEvent(commandCall.getResource(), commandCall.getElement());
    }

    public void finish(CommandCall commandCall) {
        announceAfterProcessingEvent(commandCall.getResource(), commandCall.getElement());
    }

    @Override
    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        throw new IllegalStateException("Unexpected call to " + getClass().getSimpleName() + "'s verify() method. Only the execute() method should be called.");
    }

    private List<SpecificationProcessingListener> listeners = new ArrayList<SpecificationProcessingListener>();
    private SpecificationDescriber specificationDescriber;
    
    public void addSpecificationListener(SpecificationProcessingListener listener) {
        listeners.add(listener);
    }

    public void removeSpecificationListener(SpecificationProcessingListener listener) {
        listeners.remove(listener);
    }

    public void setSpecificationDescriber(SpecificationDescriber specificationDescriber) {
        this.specificationDescriber = specificationDescriber;
    }

    private void announceAfterProcessingEvent(Resource resource, Element element) {
    	for (SpecificationProcessingListener listener : listeners) {
    		listener.afterProcessingSpecification(new SpecificationProcessingEvent(resource, element));
		}
    }

    private void announceBeforeProcessingEvent(Resource resource, Element element) {
    	for (SpecificationProcessingListener listener : listeners) {
    		listener.beforeProcessingSpecification(new SpecificationProcessingEvent(resource, element));
		}
    }
}
