/**
 * Created by DEV on 13.03.2017.
 */
export class PostService {

    getPosts() {

        return [

            {
                "image": "http://lorempixel.com/100/100/people/?1",
                "author": "Windward",
                "twittHandle": "@winwardstudios",
                "message": "Looking for a better company reporting or docgen app",
                "likes": 0,
                "liked": false
            },

            {
                "image": "http://lorempixel.com/100/100/people/?2",
                "author": "AngularJS News",
                "twittHandle": "@angularjs_news",
                "message": "Right Relevance : Influencers, Articles and Conversations",
                "likes": 5,
                "liked": true
            },

            {
                "image": "http://lorempixel.com/100/100/people/?3",
                "author": "UX & Bootstrap",
                "twittHandle": "@3rdwave",
                "message": "10 Reasons Why Web Projects Fail",
                "likes": 1,
                "liked": true
            }
        ];
    }
}