package edu.uoc.epcsd.productcatalog.model;

public enum OfferStatus {
    ACCEPTED,
    REJECTED;

    public boolean isAccepted() {
        return ACCEPTED == (this);
    }
}
