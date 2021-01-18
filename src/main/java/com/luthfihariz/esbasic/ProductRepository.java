package com.luthfihariz.esbasic;

import com.luthfihariz.esbasic.dto.SearchQueryDto;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {
    SearchResponse search(SearchQueryDto searchQueryDto) throws IOException;

    void save(Product product) throws IOException;

    void saveAll(List<Product> products) throws IOException;

    void delete(Integer productId) throws IOException;

    void update(Integer productId, Product newProduct) throws IOException;
}
