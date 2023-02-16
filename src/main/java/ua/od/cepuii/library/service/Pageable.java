package ua.od.cepuii.library.service;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;

/**
 * An interface for classes that support pagination by calculating the number of pages
 * given a current page and a set of filter parameters.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface Pageable {

    /**
     * Calculates the number of pages given a current page and filter parameters.
     *
     * @param currentPage the current page
     * @param filterParam the filter parameters to apply
     */
    int getPageAmount(Page currentPage, FilterParams filterParam);
}
