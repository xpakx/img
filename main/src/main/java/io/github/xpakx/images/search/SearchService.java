package io.github.xpakx.images.search;

import io.github.xpakx.images.profile.ProfileRepository;
import io.github.xpakx.images.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProfileRepository profileRepository;

    public Page<SearchResult> searchProfile(String searchString, int page, boolean shortList) {
        Pageable pageable = PageRequest.of(page, shortList ? 10 : 25);
        return profileRepository.
                findByUserUsernameContaining(searchString, pageable);
    }
}
