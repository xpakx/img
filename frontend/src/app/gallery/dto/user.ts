export interface User {
    username: String,
    description: String,
    avatarUrl: String,

    posts: number,
    followers: number,
    following: number,
    followed: boolean,
    avatar: boolean,
    owner: boolean,
}
