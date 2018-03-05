package com.example.owner.petbetter;

/**
 * Created by owner on 12/11/2017.
 */
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pet;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    Call<User> getUserWithId(@Field("user_id") long userId);


    @POST("getPosts.php")
    Call<ArrayList<Post>> getPosts();

    @POST("getServices.php")
    Call<ArrayList<Services>> getServices();

    @FormUrlEncoded
    @POST("getMessages.php")
    Call<ArrayList<Message>> getMessages(@Field("user_id") long id);

    @POST("getMessageReps.php")
    Call<ArrayList<MessageRep>> getMessageReps();

    @FormUrlEncoded
    @POST("getLatestRep.php")
    Call<String> getLatestRep(@Field("_id") String id);

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
    Call<ArrayList<LocationMarker>> loadMarkers(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getBookmarks.php")
    Call<ArrayList<LocationMarker>> getBookmarks(@Field("user_id") long userId, @Field("type") int type);

    @POST("getVeterinarians.php")
    Call<ArrayList<Veterinarian>> getVeterinarians();

    @POST("getRatings.php")
    Call<ArrayList<Rating>> getRatings();

    @POST("getAllPostReps.php")
    Call<ArrayList<PostRep>> getAllPostReps();

    @POST("getClinics.php")
    Call<ArrayList<Facility>> getClinics();

    @POST("getFollowers.php")
    Call<ArrayList<Follower>> getFollowers();


    @POST("addUser.php")
    Call<Void> addUser(@Body RequestBody user);

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
    @POST("getPostServer.php")
    Call<Post> getPostServer(@Field("user_id") long userId, @Field("date_created") String dateCreated);

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
    Call<ArrayList<Notifications>> getNotifications(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getPets.php")
    Call<ArrayList<Pet>> getPets(@Field("user_id") long userId);

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

    @POST("queryFacilities.php")
    Call<ArrayList<Facility>> queryFacilities(@Body RequestBody query);

    @POST("queryTopics.php")
    Call<ArrayList<Topic>> queryTopics(@Body RequestBody query);

    @POST("queryPosts.php")
    Call<ArrayList<Post>> queryPosts(@Body RequestBody query);

    @POST("queryVeterinarians.php")
    Call<ArrayList<Veterinarian>> queryVeterinarians(@Body RequestBody query);

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

    @POST("addRatings.php")
    Call<Void> addRatings(@Body RequestBody rateList);

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
    Call<Void> deleteFollower(@Field("topic_id") long topicId, @Field("user_id") long userId);

    @FormUrlEncoded
    @POST("checkIfFollower.php")
    Call<ArrayList<Follower>> getTopicFollowers(@Field("topic_id") long topicId);

    @POST("updateNotifications.php")
    Call<Void> updateNotifications(@Body RequestBody notifArray);

    @FormUrlEncoded
    @POST("getMessage.php")
    Call<Message> getMessage(@Field("_id") int messageId);

    @FormUrlEncoded
    @POST("deletePost.php")
    Call<Void> deletePost(@Field("user_id") long postId, @Field("date_created") String dateCreated);

    @FormUrlEncoded
    @POST("deletePostRep.php")
    Call<Void> deletePostRep(@Field("user_id") long userId, @Field("date_performed") String datePerformed);

    @FormUrlEncoded
    @POST("deleteTopic.php")
    Call<Integer> deleteTopic(@Field("_id") long topicId);


    @POST("editProfile.php")
    Call<Void> editProfile(@Body RequestBody userList);

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
    Call<Integer> addVet(
            @Field("_id") int vetId,
            @Field("user_id") int userId,
            @Field("rating") int rating,
            @Field("specialty") String specialty,
            @Field("phone_num") String phone_num
    );

    @POST("addVets.php")
    Call<Void> addVets(@Body RequestBody vetList);

    @POST("addPosts.php")
    Call<Void> addPosts(@Body RequestBody postList);

    @POST("addTopics.php")
    Call<Void> addTopics(@Body RequestBody topicList);

    @POST("addPostReps.php")
    Call<Void> addPostReps(@Body RequestBody postRepList);

    @POST("addServices.php")
    Call<Void> addServices(@Body RequestBody serviceList);

    @POST("addMarkers.php")
    Call<Void> addMarkers(@Body RequestBody markerList);

    @POST("addLocation.php")
    Call<Void> addLocation(@Body RequestBody location);

    @POST("addFacility.php")
    Call<Void> addFacility(@Body RequestBody facility);

    @POST("addFacilities.php")
    Call<Void> addFacilities(@Body RequestBody faciList);

    @POST("addFollowers.php")
    Call<Void> addFollowers(@Body RequestBody followerList);

    @POST("addMessages.php")
    Call<Void> addMessages(@Body RequestBody messageList);

    @POST("addNotifications.php")
    Call<Void> addNotifications(@Body RequestBody notifList);

    @POST("addPets.php")
    Call<Void> addPets(@Body RequestBody petList);

    @POST("addMessageReps.php")
    Call<Void> addMessageReps(@Body RequestBody messageRepList);

    @FormUrlEncoded
    @POST("getMarker.php")
    Call<LocationMarker> getMarker(@Field("bldg_name") String bldgName);

    @FormUrlEncoded
    @POST("getPostRepsFromParent.php")
    Call<ArrayList<PostRep>> getPostRepsFromParent(@Field("parent_id") long parentId);

    @FormUrlEncoded
    @POST("getPostRepFromId.php")
    Call<PostRep> getPostRepFromId(@Field("_id") long postRepId);
}
