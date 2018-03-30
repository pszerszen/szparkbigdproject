package com.pgs.spark.bigdata.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.pgs.spark.bigdata.to.ArticleTO;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.junit.Test;
import org.slf4j.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParsersTest {

    private static final Logger logger = LoggerFactory.getLogger(ParsersTest.class);

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private Map<String, String> domainsSeedsMap = ImmutableMap.of(
        "bankier.pl", "http://www.bankier.pl/wiadomosc/Gminy-kontroluja-czy-nie-marnotrawisz-pieniedzy-z-500-7410121.html",
        "biznes.pl", "http://biznes.pl/kraj/ey-ma-dobre-wiesci-dla-polski/h2t6m1",
        "egospodarka.pl", "http://www.firma.egospodarka.pl/133208,CEIDG-jakie-zmiany,1,11,1.html",
        "pb.pl", "http://nieruchomosci.pb.pl/4553083,79553,czynsze-w-warszawie-sa-z-gornej-polki",
        "wp.pl", "http://wiadomosci.wp.pl/kat,1356,title,Francja-Sympatyk-dzihadu-zamordowal-policjanta-i-jego-zone-na-oczach-ich-dziecka,wid,18377871,wiadomosc.html"
    );

    List<WebPageParser> parses = Arrays.asList(new BankierParserImpl(), new BiznesParserImpl(), new EGospodarkaParserImpl(), new PbParser(), new WpParserImpl());

    private Map<String, WebPageParser> parsers = parses.stream().collect(Collectors.toMap(WebPageParser::getParsablePage, parser -> parser));

    @Test
    public void shouldVisitPagesAndAndProduceArticleTO(){
        domainsSeedsMap.entrySet().stream().forEach(domainUrl -> {
            //given
            String html = null;

            try {
                html = TestUtil.getHtmlFromUrl(domainUrl.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }

            HtmlParseData htmlParseData = new HtmlParseData();
            htmlParseData.setHtml(html);

            //when
            ArticleTO articleTO = parsers.get(domainUrl.getKey()).parse(htmlParseData);
            //then
            assertNotNull(articleTO.getTitle());
            assertNotNull(articleTO.getContent());
            assertNotNull(articleTO.getDate());
            assertNotNull(articleTO.getTags());
        });
    }

    @Test
    public void shouldParseExampleBankierDocument() throws IOException, ParseException {
        //given
        HtmlParseData htmlParseData = new HtmlParseData();

        String html = TestUtil.readFileAsString("src/test/resources/bankierExamplePage.html");
        htmlParseData.setHtml(html);

        //when
        ArticleTO articleTO = parsers.get("bankier.pl").parse(htmlParseData);

        //then
        assertEquals("Tesla Motors pobiła Volkswagena", articleTO.getTitle());
        assertEquals(format.parse("2016-06-09 09:02:00") ,articleTO.getDate());
        assertEquals(Sets.newHashSet("Firmy: Tesla Volkswagen"), articleTO.getTags());
        assertTrue(articleTO.getTags().size()> 0);
    }

    @Test
    public void shouldParseExampleBiznesDocument() throws IOException, ParseException {
        //given
        HtmlParseData htmlParseData = new HtmlParseData();

        String html = TestUtil.readFileAsString("src/test/resources/biznesExamplePage.html");
        htmlParseData.setHtml(html);

        //when
        ArticleTO articleTO = parsers.get("biznes.pl").parse(htmlParseData);

        //then
        assertEquals("Sondaże: Brexit ma więcej zwolenników", articleTO.getTitle());
        assertEquals(format.parse("2016-06-14 07:02:23") ,articleTO.getDate());
        assertTrue(articleTO.getTags().size() == 0);
    }

    @Test
    public void shouldParseExampleEgospodarkaDocument() throws IOException, ParseException {
        //given
        HtmlParseData htmlParseData = new HtmlParseData();

        String html = TestUtil.readFileAsString("src/test/resources/egospodarkaExamplePage.html");
        htmlParseData.setHtml(html);

        //when
        ArticleTO articleTO = parsers.get("egospodarka.pl").parse(htmlParseData);

        //then
        assertEquals("CEIDG - jakie zmiany? Przy podawaniu adresu trzeba mieć do niego prawo Aktualizacja i odpowiedzialność za jej brak Wykreślanie i korekta bez postępowania", articleTO.getTitle());
        assertEquals(format.parse("2016-06-13 00:00:00") ,articleTO.getDate());
        assertTrue(articleTO.getTags().size() == 0);
    }

    @Test
    public void shouldParseExamplePbDocument() throws IOException, ParseException {
        //given
        HtmlParseData htmlParseData = new HtmlParseData();

        String html = TestUtil.readFileAsString("src/test/resources/pbExamplePage.html");
        htmlParseData.setHtml(html);

        //when
        ArticleTO articleTO = parsers.get("pb.pl").parse(htmlParseData);

        //then
        assertEquals("Mamy gorący rynek pracy", articleTO.getTitle());
        assertEquals(new Date(), articleTO.getDate());
        assertTrue(articleTO.getTags().size() == 0);
    }

    @Test
    public void shouldParseExampleWpDocument() throws IOException, ParseException {
        //given
        HtmlParseData htmlParseData = new HtmlParseData();

        String html = TestUtil.readFileAsString("src/test/resources/wpExamplePage.html");
        htmlParseData.setHtml(html);

        //when
        ArticleTO articleTO = parsers.get("wp.pl").parse(htmlParseData);

        //then
        assertEquals("Jerzy Kryszak: Ale jazda, czyli mecz w korku #dziejesienaeuro. Polacy wygrali, kto bohaterem? Eksperci wskazują dwóch piłkarzy", articleTO.getTitle());
        assertEquals(new Date() ,articleTO.getDate());
        assertTrue(articleTO.getTags().size() == 0);
    }

}