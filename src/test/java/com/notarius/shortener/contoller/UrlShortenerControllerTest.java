package com.notarius.shortener.contoller;

import com.notarius.shortener.AbstractTest;
import com.notarius.shortener.ShortenerApplicationTests;
import com.notarius.shortener.controller.UrlController;
import com.notarius.shortener.dto.UrlDto;
import com.notarius.shortener.entity.Url;
import com.notarius.shortener.service.UrlShortenerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = ShortenerApplicationTests.class)
public class UrlShortenerControllerTest extends AbstractTest {

    public static final String URI = "http://localhost:8080/urls";
    public static final String GET_BY_SHORTENER_URI = "http://localhost:8080/urls/1234";
    public static final String SHORTENER_URI = "1234";
    public static final String ORIGINAL_URI = "TEST";
    @InjectMocks
    UrlController urlController;
    @Mock
    UrlShortenerService urlShortenerService;
    MockMvc mockMvc;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();

    }

    @Test
    public void listAllUrl() throws Exception {
        List<UrlDto> expected = null;
        String expectedString = super.mapToJson(expected);

        Mockito.when(urlShortenerService.listAll()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get(URI).contentType(MediaType.APPLICATION_JSON)
                                .content(expectedString)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    public void saveUrl() throws Exception {
        UrlDto urlDtoToSave = UrlDto.builder().originalUrl(ORIGINAL_URI).build();
        String requestJson = super.mapToJson(urlDtoToSave);

        Mockito.when(urlShortenerService.saveShortenUrl(Url.builder().originalUrl(ORIGINAL_URI).build())).thenReturn(urlDtoToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void getUrlByShortenerUrl() throws Exception {
        UrlDto expectedUrlDto = UrlDto.builder().originalUrl(SHORTENER_URI).build();

        Mockito.when(urlShortenerService.getUrlByShortenedUrl(SHORTENER_URI)).thenReturn(expectedUrlDto);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_BY_SHORTENER_URI))
                .andExpect(status().isOk())
                .andDo(print());
    }

}