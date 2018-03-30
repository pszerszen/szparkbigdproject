package com.pgs.spark.bigdata.service;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.PagableList;
import facebook4j.Post;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Consumer;

@Service
public class FacebookCrawlingService {

    private static final Logger LOG = LoggerFactory.getLogger(FacebookCrawlingService.class);

    @Autowired
    private Facebook facebook;

    @Autowired
    private DocumentService documentService;

    @Value("${fb.search.terms}")
    private Set<String> facebookSearchTerms;

    public void crawlFacebook() {
        try {
            facebook.getHome().forEach(this::handlePost);
            facebook.getFeed().forEach(this::handlePost);

            for(String term : facebookSearchTerms){
                facebook.searchPosts(term).forEach(this::handlePost);
            }

        } catch (FacebookException e) {
            LOG.error("Exception while searching facebook:", e);
        }
    }

    private void handlePost(final Post post) {
        StringBuilder postContetnt = new StringBuilder()
                .append("Post:\n")
                .append(post.getMessage())
                .append("Likes: ").append(post.getLikes().getCount());

        final PagableList<Comment> comments = post.getComments();

        //TODO: discuss if we even want comments
        if (CollectionUtils.isNotEmpty(comments)) {
            postContetnt.append("Comments:\n");
            comments.forEach(commentContentAppender(postContetnt));
        }
        documentService.addDocument(post.getSource().toString(), postContetnt.toString(), post.getCreatedTime(), post.getUpdatedTime());
    }

    private Consumer<Comment> commentContentAppender(StringBuilder postContent) {
        return (comment) -> postContent
                .append("\tComment:")
                .append("\t\tFrom: ").append(comment.getFrom().getName())
                .append("\t\tMessage: ").append(comment.getMessage())
                .append("\t\tLikes: ").append(comment.getLikeCount());
    }
}
