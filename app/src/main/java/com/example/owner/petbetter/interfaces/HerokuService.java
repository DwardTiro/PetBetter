package com.example.owner.petbetter;

/**
 * Created by owner on 12/11/2017.
 */
import com.example.owner.petbetter.classes.Bookmark;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.FacilityMembership;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.LocationMarker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Pet;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.Rating;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.Upvote;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.classes.WorkHours;

import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @FormUrlEncoded
    @POST("getFilteredPosts.php")
    Call<ArrayList<Post>> getFilteredPosts(@Field("order") int order);

    @FormUrlEncoded
    @POST("getFilteredUserPosts.php")
    Call<ArrayList<Post>> getFilteredUserPosts(@Field("order") int order, @Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getUserTopics.php")
    Call<ArrayList<Topic>> getUserTopics(@Field("creator_id") long creatorId);

    @FormUrlEncoded
    @POST("getFacilityBookmarks.php")
    Call<ArrayList<Facility>> getFacilityBookmarks(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("getPostBookmarks.php")
    Call<ArrayList<Post>> getPostBookmarks(@Field("user_id") long userId);

    @POST("getServices.php")
    Call<ArrayList<Services>> getServices();

    @FormUrlEncoded
    @POST("getMessages.php")
    Call<ArrayList<Message>> getMessages(@Field("user_id") long id);

    @POST("getLatestMessage.php")
    Call<Message> getLatestMessage();

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

    @FormUrlEncoded
    @POST("getPendingByUser.php")
    Call<ArrayList<Pending>> getPendingByUser(@Field("foreign_id") long foreignId);

    @FormUrlEncoded
    @POST("getPendingFacility.php")
    Call<ArrayList<Pending>> getPendingFacility(@Field("foreign_id") long foreignId, @Field("type") int type);

    @FormUrlEncoded
    @POST("editPending.php")
    Call<Void> editPending(@Field("foreign_id") long foreignId, @Field("specialty") String specialty);

    @POST("loadLocations.php")
    Call<ArrayList<LocationMarker>> loadLocations();

    @FormUrlEncoded
    @POST("getVetsByFacility.php")
    Call<ArrayList<Veterinarian>> getVetsByFacility(@Field("faci_id") long faci_id);

    @FormUrlEncoded
    @POST("getVeterinarians.php")
    Call<ArrayList<Veterinarian>> getVeterinarians(@Field("checkdisabled") int checkdisabled);


    @FormUrlEncoded
    @POST("getVeterinarianWithId.php")
    Call<Veterinarian> getVeterinarianWithId(@Field("user_id") long userId);

    @POST("getRatings.php")
    Call<ArrayList<Rating>> getRatings();

    @POST("getAllPostReps.php")
    Call<ArrayList<PostRep>> getAllPostReps();

    @POST("getUsers.php")
    Call<ArrayList<User>> getUsers();

    @FormUrlEncoded
    @POST("getClinics.php")
    Call<ArrayList<Facility>> getClinics(@Field("checkdisabled") int checkdisabled);

    @POST("getPending.php")
    Call<ArrayList<Pending>> getPending();

    @POST("getFollowers.php")
    Call<ArrayList<Follower>> getFollowers();

    @POST("getFacilityMembers.php")
    Call<ArrayList<FacilityMembership>> getFacilityMembers();

    @FormUrlEncoded
    @POST("getTopicFollowers.php")
    Call<ArrayList<Follower>> getAcceptedFollowers(@Field("topic_id") long topicId);

    @FormUrlEncoded
    @POST("getPendingFollowers.php")
    Call<ArrayList<Follower>> getPendingFollowers(@Field("topic_id") long topicId);

    @FormUrlEncoded
    @POST("getAllowedFollowers.php")
    Call<ArrayList<Follower>> getAllowedFollowers(@Field("topic_id") long topicId);

    @POST("addUser.php")
    Call<ResponseBody> addUser(@Body RequestBody user);

    @FormUrlEncoded
    @POST("getServicesWithFaciID.php")
    Call<ArrayList<Services>> getServicesWithFaciID(@Field("faci_id") long faci_id);

    @FormUrlEncoded
    @POST("getWorkhoursWithFaciId.php")
    Call<ArrayList<WorkHours>> getWorkhoursWithFaciID(@Field("faci_id") long faci_id);


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

    @POST("getFilteredTopics.php")
    Call<ArrayList<Topic>> getFilteredTopics();

    @POST("getAllTopics.php")
    Call<ArrayList<Topic>> getAllTopics();

    @POST("getAllPosts.php")
    Call<ArrayList<Post>> getAllPosts();

    @POST("getUpvotes.php")
    Call<ArrayList<Upvote>> getUpvotes();

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

    @POST("queryUsers.php")
    Call<ArrayList<User>> queryUsers(@Body RequestBody query);

    @FormUrlEncoded
    @POST("queryMessages.php")
    Call<ArrayList<Message>> queryMessages(@Field("queryjson") String query, @Field("_id") long _id);

    @FormUrlEncoded
    @POST("queryPending.php")
    Call<ArrayList<Pending>> queryPending(@Field("queryjson") String query, @Field("type") int type);

    @POST("queryEmail.php")
    Call<ArrayList<String>> queryEmail();

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
    @POST("deleteMessage.php")
    Call<Void> deleteMessage(@Field("_id") long messageId);

    @FormUrlEncoded
    @POST("deleteBookmark.php")
    Call<Void> deleteBookmark(@Field("user_id") long user_id, @Field("item_id") long item_id, @Field("bookmark_type") long bookmark_type);

    @FormUrlEncoded
    @POST("deleteFollower.php")
    Call<Void> deleteFollower(@Field("topic_id") long topicId, @Field("user_id") long userId);

    @FormUrlEncoded
    @POST("deleteService.php")
    Call<Void> deleteService(@Field("_id") long service_id);

    @FormUrlEncoded
    @POST("disableTopic.php")
    Call<Void> disableTopic(@Field("is_deleted") int isDeleted, @Field("_id") long topicId);

    @FormUrlEncoded
    @POST("setIsApproved.php")
    Call<Void> setIsApproved(@Field("is_approved") int isApproved, @Field("_id") long pendingId);

    @FormUrlEncoded
    @POST("disablePost.php")
    Call<Void> disablePost(@Field("is_deleted") int isDeleted, @Field("_id") long postId);

    @FormUrlEncoded
    @POST("disableUser.php")
    Call<Void> disableUser(@Field("is_disabled") int isDisabled, @Field("user_id") long userId);

    @FormUrlEncoded
    @POST("disableFacility.php")
    Call<Void> disableFacility(@Field("is_disabled") int isDisabled, @Field("faci_id") long faciId);



    @FormUrlEncoded
    @POST("deleteNotification.php")
    Call<Void> deleteNotification(@Field("source_id") long sourceId, @Field("doer_id") long doerId, @Field("type") int type);

    @FormUrlEncoded
    @POST("updateRating.php")
    Call<Void> updateRating(@Field("_id") long id, @Field("value") float value, @Field("type") int type);

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

    @POST("editFacilities.php")
    Call<Void> editFacilities(@Body RequestBody faciList);

    @POST("editFacilityService.php")
    Call<Void> editFacilityService(@Body RequestBody serviceList);

    @FormUrlEncoded
    @POST("editVeterinarian.php")
    Call<Void> editVeterinarian(@Field("specialty") String specialty,
                                @Field("education") String education,
                                @Field("profile_desc") String profile_desc,
                                @Field("user_id") long user_id);

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
    Call<Void> addVet(@Field("user_id") long user_id,
                      @Field("specialty") String specialty,
                      @Field("rating") float rating,
                      @Field("education") String education,
                      @Field("is_licensed") int isLicensed,
                      @Field("profile_desc") String profileDesc);

    @POST("addVets.php")
    Call<Void> addVets(@Body RequestBody vetList);

    @POST("addMembers.php")
    Call<Void> addMembers(@Body RequestBody fmList);

    @POST("addPosts.php")
    Call<Void> addPosts(@Body RequestBody postList);

    @POST("addTopics.php")
    Call<Void> addTopics(@Body RequestBody topicList);

    @POST("deleteTopics.php")
    Call<Void> deleteTopics(@Body RequestBody topicList);

    @POST("deletePosts.php")
    Call<Void> deletePosts(@Body RequestBody postList);

    @POST("addUpvotes.php")
    Call<Void> addUpvotes(@Body RequestBody upvoteList);

    @POST("addBookmark.php")
    Call<Void> addBookmark(@Body RequestBody bookmark);

    @POST("addBookmarks.php")
    Call<Void> addBookmarks(@Body RequestBody bookmarkList);

    @POST("addPostReps.php")
    Call<Void> addPostReps(@Body RequestBody postRepList);

    @POST("addServices.php")
    Call<Void> addServices(@Body RequestBody serviceList);

    @POST("addWorkhours.php")
    Call<Void> addWorkhours(@Body RequestBody hoursList);

    @POST("addLocations.php")
    Call<Void> addLocations(@Body RequestBody locationList);

    @POST("addMarkers.php")
    Call<Void> addMarkers(@Body RequestBody markerList);

    @POST("addLocation.php")
    Call<Void> addLocation(@Body RequestBody location);

    @POST("addFacility.php")
    Call<Void> addFacility(@Body RequestBody facility);

    @POST("addFacilities.php")
    Call<Void> addFacilities(@Body RequestBody faciList);

    @POST("addPending.php")
    Call<Void> addPending(@Body RequestBody pendingList);

    @POST("addFollowers.php")
    Call<Void> addFollowers(@Body RequestBody followerList);

    @POST("editFollowers.php")
    Call<Void> editFollowers(@Body RequestBody follower);

    @POST("editMessage.php")
    Call<Void> editMessage(@Body RequestBody message);

    @POST("addMessages.php")
    Call<Void> addMessages(@Body RequestBody messageList);

    @POST("addNotifications.php")
    Call<Void> addNotifications(@Body RequestBody notifList);

    @POST("addPets.php")
    Call<Void> addPets(@Body RequestBody petList);

    @POST("addMessageReps.php")
    Call<Void> addMessageReps(@Body RequestBody messageRepList);

    @POST("addUsers.php")
    Call<Void> addUsers(@Body RequestBody userList);

    @FormUrlEncoded
    @POST("getMarker.php")
    Call<LocationMarker> getMarker(@Field("bldg_name") String bldgName);

    @FormUrlEncoded
    @POST("getMarkerWithFaciId.php")
    Call<LocationMarker> getMarkerWithFaciId(@Field("faci_id") long faciId);

    @FormUrlEncoded
    @POST("removeVote.php")
    Call<Void> removeVote(@Field("_id") long id);

    @FormUrlEncoded
    @POST("alterVote.php")
    Call<Void> alterVote(@Field("_id") long id, @Field("value") int value);

    @FormUrlEncoded
    @POST("getPostRepsFromParent.php")
    Call<ArrayList<PostRep>> getPostRepsFromParent(@Field("parent_id") long parentId);

    @FormUrlEncoded
    @POST("getPostRepFromId.php")
    Call<PostRep> getPostRepFromId(@Field("_id") long postRepId);
}
