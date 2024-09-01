export interface ImageDetails {
  id: String,
  caption?: String,
  imageUrl: String,
  createdAt: Date,
  author: String,
  likes: number,
  comments: number,
  liked: boolean,
  owner: boolean,
}
