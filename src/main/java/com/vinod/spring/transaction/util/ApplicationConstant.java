package com.vinod.spring.transaction.util;

public final class ApplicationConstant {

    /**
     * Instantiates a new application constant.
     */
    private ApplicationConstant() {
    }

    //Endpoint
    public static final String CUSTOMER_SERVICE = "/customers";

    /**
     * The Enum Customer Status.
     */
    public enum CustomerStatus {
        PENDING("Pending"),
        REGISTERED("Registered"),
        BLOCKED("Blocked");

        /** The value. */
        private final String value;

        CustomerStatus(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
