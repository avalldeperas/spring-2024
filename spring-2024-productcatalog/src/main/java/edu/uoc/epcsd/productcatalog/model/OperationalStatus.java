package edu.uoc.epcsd.productcatalog.model;

public enum OperationalStatus {

    OPERATIONAL,
    NON_OPERATIONAL;

    public boolean isOperational() {
        return OPERATIONAL == (this);
    }
}
