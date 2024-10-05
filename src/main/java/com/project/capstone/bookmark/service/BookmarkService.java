package com.project.capstone.bookmark.service;

import com.project.capstone.bookmark.repository.BookmarkRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

}
