import Post from "./Post";
import Share from "./Share";
import "./feed.css";
import { useEffect, useState } from "react";
import { retrievePostImages } from "../services/posts";

export default function Feed() {

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(false)
  const [posts, setPosts] = useState([])

  useEffect(() => {
    setLoading(true);
    let session = localStorage.getItem("sessionData")
    let sessionData = JSON.parse(session);

    async function getPostData(sessionData){
      let response = await retrievePostImages(sessionData);
      
      if (response == "error"){
        setError(true);
      }

      if (response.length > 0){
        setPosts(response);
      }
    }

    getPostData(sessionData);

    setLoading(false);

  }, [])

  return (
      <div className="feedWrapper">
        <Share />
        {loading && <p>loading ...</p>}
        {error && <p>could not load feed</p>}
        {posts.map((p) => (
          <Post key={p.digitalSignature} post={p} />
        ))}
      </div>
  );
}
