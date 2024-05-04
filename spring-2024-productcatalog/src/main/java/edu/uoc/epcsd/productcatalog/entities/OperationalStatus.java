package edu.uoc.epcsd.productcatalog.entities;

public enum OperationalStatus {

    OPERATIONAL,
    NON_OPERATIONAL;

    public boolean isOperational() {
        return OPERATIONAL == (this);
    }
}
