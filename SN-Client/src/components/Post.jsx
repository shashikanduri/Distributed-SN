import "./post.css";
import "./share.css"

export default function Post({ post }) {

  return (
    <div className="post">
      <div className="postWrapper">
        <div className="postTop">
          <div className="postTopLeft">
            <img
              className="postProfileImg"
              src="assets/person/2.jpeg"
              alt=""
            />
            <span className="postUsername">
              {post.name}
            </span>            
          </div>
        </div>   
        {post.imageText && <span className="postText">{post.imageText}</span> }
        <div className="postCenter">
          {/* {!showPost && <button type="button" className="shareButton" onClick={viewPost} >View Post</button>} */}
          {<img className="postImg" src={post.imageData}  alt="" />}
        </div>
      </div>
    </div>
  );
}
