package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.Post;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestDataUtils {

    public static Stream<Arguments> loadPostsFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Post[] posts = mapper.readValue(new File(path), Post[].class);
        return Arrays.stream(posts).map(Arguments::of);
    }
}
