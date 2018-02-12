package com.example.owner.petbetter;

/**
 * Created by owner on 12/11/2017.
 */
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HerokuService {

    @FormUrlEncoded
    @POST("checkLogin.php")
    Call<User> checkLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("getUserId.php")
    Call<User> getUserId(@Field("email") String email);

    @FormUrlEncoded
    @POST("getPostUser.php")
    Call<User> getPostUser(@Field("_id") long id);

    @FormUrlEncoded
    @POST("getUserName.php")
    Call<User> getUserName(@Field("email") String email);

    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUser(@Field("email") String email);

    @FormUrlEncoded
    @POST("getUserWithId.php")
    Call<User> getUserWithId(@Field("email") String email);

    @FormUrlEncoded
    @POST("getPosts.php")
    Call<ArrayList<Post>> getPosts();

    @FormUrlEncoded
    @POST("getMessages.php")
    Call<ArrayList<Message>> getMessages(@Field("_id") long id);

    @FormUrlEncoded
    @POST("getLatestRep.php")
    Call<String> getLatestRep(@Field("_id") String id);

    @FormUrlEncoded
    @POST("getMessageReps.php")
    Call<MessageRep> getMessageReps(@Field("_id") long id);

    @FormUrlEncoded
    @POST("getPostReps.php")
    Call<ArrayList<PostRep>> getPostReps(@Field("post_id") int postId);

    @FormUrlEncoded
    @POST("getMarkerIds.php")
    Call<ArrayList<Integer>> getMarkerIds();

    @FormUrlEncoded
    @POST("getUserIds.php")
    Call<ArrayList<Integer>> getUserIds();

    @FormUrlEncoded
    @POST("getVetIds.php")
    Call<ArrayList<Integer>> getVetIds();

    @FormUrlEncoded
    @POST("loadMarkers.php")
    Call<ArrayList<Marker>> loadMarkers(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getBookmarks.php")
    Call<ArrayList<Marker>> getBookmarks(@Field("user_id") long userId, @Field("type") int type);

    @POST("getVeterinarians.php")
    Call<ArrayList<Veterinarian>> getVeterinarians();

    @POST("getClinics.php")
    Call<ArrayList<Facility>> getClinics();

    @POST("getFollowers.php")
    Call<ArrayList<Follower>> getFollowers();

    @FormUrlEncoded
    @POST("addUser.php")
    Call<Integer> addUser(@Field("_id") int userId, @Field("first_name") String firstName, @Field("last_name") String lastName,
                       @Field("mobile_num") String mobileNum, @Field("phone_num") String phoneNum, @Field("email") String email,
                       @Field("password") String password, @Field("user_type") int userType);

    @FormUrlEncoded
    @POST("touchMarker.php")
    Call<Integer> touchMarker(@Field("_id") int rowId, @Field("bldg_name") String bldgName, @Field("longitude") double longitude,
                       @Field("latitude") String latitude, @Field("location") String location, @Field("user_id") long userId,
                       @Field("type") int type);

    @FormUrlEncoded
    @POST("getPostRepIds.php")
    Call<ArrayList<Integer>> getPostRepIds();

    @FormUrlEncoded
    @POST("getMessageRepIds.php")
    Call<ArrayList<Integer>> getMessageRepIds();

    @FormUrlEncoded
    @POST("addPostRep.php")
    Call<Integer> addPostRep(@Field("_id") int postRepId, @Field("user_id") int userId, @Field("post_id") int post_id,
                           @Field("parent_id") String parentId, @Field("rep_content") String repContent,
                          @Field("date_performed") String datePerformed);

    @FormUrlEncoded
    @POST("getPost.php")
    Call<Post> getPost(@Field("_id") long postId);

    @FormUrlEncoded
    @POST("getPostReps.php")
    Call<ArrayList<PostRep>> getPostReps(@Field("post_id") long postId);

    @FormUrlEncoded
    @POST("addMessageRep.php")
    Call<Integer> addMessageRep(@Field("_id") int messageRepId, @Field("user_id") int userId, @Field("message_id") int messageId,
                             @Field("rep_content") String repContent, @Field("is_sent") int isSent,
                             @Field("date_performed") String datePerformed);

    @FormUrlEncoded
    @POST("getNotifications.php")
    Call<ArrayList<PostRep>> getNotifications(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getTopics.php")
    Call<ArrayList<Topic>> getTopics();

    @FormUrlEncoded
    @POST("createMessage.php")
    Call<Integer> createMessage(@Field("_id") int messageId, @Field("user_one") long userId, @Field("user_two") long toId);

    @FormUrlEncoded
    @POST("getMessageIds.php")
    Call<ArrayList<Integer>> getMessageIds();

    @FormUrlEncoded
    @POST("generateMessageRepIds.php")
    Call<ArrayList<Integer>> generateMessageRepIds();

    @FormUrlEncoded
    @POST("getNotifIds.php")
    Call<ArrayList<Integer>> getNotifIds();

    @FormUrlEncoded
    @POST("notifyUser.php")
    Call<Integer> notifyUser(@Field("_id") int notifId, @Field("user_id") long toId, @Field("doer_id") long userId,
                          @Field("is_read") int isRead, @Field("type") int type, @Field("date_performed") String timeStamp,
                          @Field("source_id") long sourceId);

    @FormUrlEncoded
    @POST("getTopicIds.php")
    Call<ArrayList<Integer>> getTopicIds();

    @FormUrlEncoded
    @POST("createTopic.php")
    Call<Integer> createTopic(@Field("_id") int topicId, @Field("creator_id") long creatorId, @Field("topic_name") String topicName,
                          @Field("topic_desc") int topicDesc, @Field("date_created") int dateCreated,
                           @Field("is_deleted") int isDeleted);

    @FormUrlEncoded
    @POST("getTopicPosts.php")
    Call<Integer> createTopic(@Field("topic_id") long topicId);

    @FormUrlEncoded
    @POST("getPostIds.php")
    Call<ArrayList<Integer>> getPostIds();

    @FormUrlEncoded
    @POST("createPost.php")
    Call<Integer> createPost(@Field("_id") int pId, @Field("user_id") long userId, @Field("topic_name") String postTitle,
                           @Field("topic_content") String postDesc, @Field("topic_id") long topicId,
                           @Field("date_created") String dateCreated, @Field("is_deleted") int isDeleted);

    @FormUrlEncoded
    @POST("getPostsWithUserId.php")
    Call<Integer> getPostsWithUserId(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("createVetRating.php")
    Call<Integer> createVetRating(@Field("_id") int pId, @Field("rater_id") long userId, @Field("rated_id") long vet_id,
                          @Field("rating") float rating, @Field("comment") String comment,
                          @Field("date_created") String dateCreated, @Field("is_deleted") int isDeleted);

    @FormUrlEncoded
    @POST("createFacilityRating.php")
    Call<Integer> createFacilityRating(@Field("_id") int pId, @Field("rater_id") long userId, @Field("rated_id") long vet_id,
                               @Field("rating") float rating, @Field("comment") String comment,
                               @Field("date_created") String dateCreated, @Field("is_deleted") int isDeleted);

    @FormUrlEncoded
    @POST("getRatingIds.php")
    Call<ArrayList<Integer>> getRatingIds();

    @FormUrlEncoded
    @POST("getVeterinarianRatings.php")
    Call<ArrayList<Float>> getVeterinarianRatings(@Field("rated_id") int vet_id);

    @FormUrlEncoded
    @POST("getFacilityRatings.php")
    Call<ArrayList<Float>> getFacilityRatings(@Field("rated_id") int facility_id);

    @FormUrlEncoded
    @POST("setNewVetRating.php")
    Call<Integer> setNewVetRating(@Field("rating") float newRating, @Field("_id") int vet_id);

    @FormUrlEncoded
    @POST("setNewFacilityRating.php")
    Call<Integer> setNewFacilityRating(@Field("rating") float newRating, @Field("_id") int vet_id);

    @FormUrlEncoded
    @POST("getFollowerIds.php")
    Call<ArrayList<Integer>> getFollowerIds();

    @FormUrlEncoded
    @POST("addFollower.php")
    Call<Integer> addFollower(@Field("_id") int followerId, @Field("topic_id") int topicId, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("getFollowerCount.php")
    Call<Integer> getFollowerCount(@Field("topic_id") int topicId);

    @FormUrlEncoded
    @POST("checkIfFollowerCount.php")
    Call<Integer> checkIfFollowerCount(@Field("_id") int topicId);

    @FormUrlEncoded
    @POST("checkIfFollower.php")
    Call<Integer> checkIfFollower(@Field("topic_id") int topicId, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("deleteFollower.php")
    Call<Integer> deleteFollower(@Field("topic_id") int topicId, @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("checkIfFollower.php")
    Call<ArrayList<Follower>> getTopicFollowers(@Field("topic_id") long topicId);

    @FormUrlEncoded
    @POST("notifRead.php")
    Call<Integer> notifRead(@Field("topic_id") long notifId);

    @FormUrlEncoded
    @POST("getMessage.php")
    Call<Message> getMessage(@Field("_id") int messageId);

    @FormUrlEncoded
    @POST("deletePost.php")
    Call<Integer> deletePost(@Field("_id") long postId);

    @FormUrlEncoded
    @POST("deletePostRep.php")
    Call<Integer> deletePostRep(@Field("_id") long postRepId);

    @FormUrlEncoded
    @POST("deleteTopic.php")
    Call<Integer> deleteTopic(@Field("_id") long topicId);

    @FormUrlEncoded
    @POST("editProfile.php")
    Call<Integer> editProfile(@Field("_id") long _id, @Field("first_name") String firstName, @Field("last_name") String lastName,
                           @Field("email") String emailAddress, @Field("mobile_num") String mobileNum,
                           @Field("phone_num") String phoneNum);

    @FormUrlEncoded
    @POST("getMessageId.php")
    Call<Integer> getMessageId(@Field("from_id") long fromId, @Field("to_id") long toId);

    @FormUrlEncoded
    @POST("generateFaciIds.php")
    Call<ArrayList<Integer>> generateFaciIds();

    @FormUrlEncoded
    @POST("generateVetIds.php")
    Call<ArrayList<Integer>> generateVetIds();

    @FormUrlEncoded
    @POST("convertFaciToBookmark.php")
    Call<Integer> convertFaciToBookmark(@Field("_id") int mId, @Field("bldg_name") String bldgName,
                                     @Field("longitude") double longitude, @Field("latitude") double latitude,
                                     @Field("location") String location, @Field("user_id") long userId,
                                     @Field("type") int type);

    @FormUrlEncoded
    @POST("convertBookmarkToFaci.php")
    Call<Integer> convertBookmarkToFaci(@Field("_id") int fId, @Field("faci_name") String faciName,
                                     @Field("location") String location, @Field("vet_id") long userId,
                                     @Field("rating") int rating);

    @FormUrlEncoded
    @POST("getFacility.php")
    Call<Facility> getFacility(@Field("_id") int id);

    @FormUrlEncoded
    @POST("addVet.php")
    Call<Integer> addVet(@Field("_id") int vetId, @Field("user_id") int userId,
                                     @Field("rating") int rating);

    @POST("addVets.php")
    Call<Void> addVets(@Body ArrayList<Veterinarian> vetList);

    @POST("addFacilities.php")
    Call<Void> addFacilities(@Body ArrayList<Facility> faciList);

    @POST("addFollowers.php")
    Call<Void> addFollowers(@Body ArrayList<Follower> followerList);

    @FormUrlEncoded
    @POST("getMarker.php")
    Call<Marker> getMarker(@Field("bldg_name") String bldgName);

    @FormUrlEncoded
    @POST("getPostRepsFromParent.php")
    Call<ArrayList<PostRep>> getPostRepsFromParent(@Field("parent_id") long parentId);

    @FormUrlEncoded
    @POST("getPostRepFromId.php")
    Call<PostRep> getPostRepFromId(@Field("_id") long postRepId);
}
