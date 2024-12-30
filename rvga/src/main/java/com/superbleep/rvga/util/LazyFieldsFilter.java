package com.superbleep.rvga.util;

import jakarta.persistence.Persistence;

public class LazyFieldsFilter {
    @Override
    public boolean equals(Object obj) {
        return !Persistence.getPersistenceUtil().isLoaded(obj);
    }
}
