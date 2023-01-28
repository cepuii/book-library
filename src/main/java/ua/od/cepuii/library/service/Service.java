package ua.od.cepuii.library.service;

import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;

public interface Service {
    int getPageAmount(Page currentPage, FilterParams filterParam);
}
