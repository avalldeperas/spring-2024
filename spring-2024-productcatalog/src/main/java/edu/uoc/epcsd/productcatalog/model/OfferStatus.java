package edu.uoc.epcsd.productcatalog.model;

public enum OfferStatus {
    ACCEPTED,
    REJECTED,
    PENDING;

    public boolean isAccepted() {
        return ACCEPTED == (this);
    }
}
