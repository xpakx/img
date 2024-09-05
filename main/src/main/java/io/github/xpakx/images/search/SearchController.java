package io.github.xpakx.images.search;

import io.github.xpakx.images.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping("/search")
    public Page<SearchResult> searchUsers(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam(name = "short", required = false, defaultValue = "false") boolean shortList
    ) {
        return service.searchProfile(query, page, shortList);
    }
}
