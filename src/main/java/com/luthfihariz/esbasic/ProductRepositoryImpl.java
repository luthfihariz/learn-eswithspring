package com.luthfihariz.esbasic;

import com.luthfihariz.esbasic.dto.FilterRequestDto;
import com.luthfihariz.esbasic.dto.RangeFilterDto;
import com.luthfihariz.esbasic.dto.SearchQueryDto;
import com.luthfihariz.esbasic.dto.SortOrder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    private static final String INDEX_NAME = "product";

    @Override
    public SearchResponse search(SearchQueryDto searchQuery) throws IOException {
        SearchRequest searchRequest = Requests.searchRequest(INDEX_NAME);

        // boolean query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("description", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("category", searchQuery.getQuery()));


        // facet query
        if (searchQuery.getFilter() != null) {
            FilterRequestDto filter = searchQuery.getFilter();
            if (filter.getRange() != null) {
                for (String keyToFilter : filter.getRange().keySet()) {
                    RangeFilterDto valueToFilter = filter.getRange().get(keyToFilter);

                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(keyToFilter);

                    if (valueToFilter.getLte() != null) {
                        rangeQueryBuilder.lte(valueToFilter.getLte());
                    }

                    if (valueToFilter.getLt() != null) {
                        rangeQueryBuilder.lt(valueToFilter.getLt());
                    }

                    if (valueToFilter.getGt() != null) {
                        rangeQueryBuilder.gt(valueToFilter.getGt());
                    }

                    if (valueToFilter.getGte() != null) {
                        rangeQueryBuilder.gte(valueToFilter.getGte());
                    }

                    boolQueryBuilder.filter(rangeQueryBuilder);
                }

            } else if (filter.getMatch() != null) {
                for (String keyToFilter : filter.getMatch().keySet()) {
                    Object valueToFilter = filter.getMatch().get(keyToFilter).toString().toLowerCase();

                    boolQueryBuilder.filter(QueryBuilders.termQuery(keyToFilter, valueToFilter));
                }
            }
        }

        // pagination
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from(searchQuery.getPage() * searchQuery.getSize())
                .size(searchQuery.getSize())
                .query(boolQueryBuilder);

        // sorting
        if (searchQuery.getSort() != null) {
            var sortOrder = org.elasticsearch.search.sort.SortOrder.ASC;
            if (searchQuery.getSort().getOrder().equals(SortOrder.DESCENDING)) {
                sortOrder = org.elasticsearch.search.sort.SortOrder.DESC;
            }
            searchSourceBuilder.sort(searchQuery.getSort().getField() + ".keyword", sortOrder);
        }

        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void save(Product product) throws IOException {
        IndexRequest indexRequest = Requests.indexRequest(INDEX_NAME)
                .id(product.getId().toString())
                .source(product);

        RequestOptions options = RequestOptions.DEFAULT;
        restHighLevelClient.index(indexRequest, options);
    }

    @Override
    public void saveAll(List<Product> products) throws IOException {

        BulkRequest bulkRequest = Requests.bulkRequest();
        IndexRequest indexRequest = Requests
                .indexRequest(INDEX_NAME)
                .source(products);
        bulkRequest.add(indexRequest);

        AtomicReference<Integer> failureCounter = new AtomicReference<>(0);

        RequestOptions options = RequestOptions.DEFAULT;
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, options);
        responses.forEach(response -> {
            if (response.isFailed()) {
                failureCounter.getAndSet(failureCounter.get() + 1);
            }
        });


    }

    @Override
    public void delete(Integer productId) throws IOException {
        DeleteRequest deleteRequest = Requests.deleteRequest(INDEX_NAME);
        deleteRequest.id(productId.toString());
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void update(Integer productId, Product newProduct) throws IOException {
        delete(productId);
        save(newProduct);
    }
}
