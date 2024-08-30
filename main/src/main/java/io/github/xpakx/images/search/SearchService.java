package io.github.xpakx.images.search;

import io.github.xpakx.images.profile.Profile;
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

    public Page<SearchResult> searchProfile(String searchString, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return profileRepository.
                findByUserUsernameContaining(searchString, pageable)
                .map(this::toDto);
    }

    private SearchResult toDto(Profile profile) {
        return new SearchResult(
                profile.getUser().getUsername(),
                profile.getDescription()
        );
    }
}
