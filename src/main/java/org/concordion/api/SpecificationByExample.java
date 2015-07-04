package org.concordion.api;

import java.util.List;

/**
 *
 * This interface changed in concordion 2.0 - several new methods were implemented. Default versions of these methods
 * that mirror existing functionality are available by extending AbstractSpecification - instead of implementing
 * Specification.
 *
 */
public interface SpecificationByExample extends Specification {

    void setFixtureClass(Class<?> fixture);

    void processExample(Evaluator evaluator, String example, ResultRecorder resultRecorder);

    List<String> getExampleNames();
}