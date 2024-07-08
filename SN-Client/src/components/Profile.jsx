import "./profile.css";
import Topbar from "./Topbar";
import Feed from "./Feed";

export default function Profile() {
  return (
    <>
      <Topbar />
      <div className="main">        
        <div className="content">
            <Feed />            
        </div>
      </div>
    </>
  );
}
