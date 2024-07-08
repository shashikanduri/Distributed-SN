import axios from "axios";
import { AESDecryption } from "./security";

let pdsStatus = null
let snStatus = null
let signupStatus = null
let loginStatus = null
let postDataResponse = null
let signupStatusSN = null

export async function postPDS(formData, url){

    pdsStatus = await axios.post(url,formData).catch((e) => { pdsStatus = e} )
    return pdsStatus
}

export async function postSN(formData){

      snStatus = await axios.post("http://localhost:8082/api/users/SavePost",formData).catch((e) => {snStatus = e})
      return snStatus
}
    
export async function signup(formData, url){
    signupStatus = await axios.post(url,formData).catch((e) => { signupStatus = e })
    return signupStatus
}

export async function signupSN(formData, url){
    signupStatusSN = await axios.post(url,formData).catch((e) => { signupStatusSN = e })
    return signupStatusSN
}

export async function login(formData, url){

    loginStatus = await axios.post(url,formData).catch(e => {loginStatus = e})
    return loginStatus
}

export async function getpost(sig, email){

    let url = "http://localhost:8080/api/posts/getpost"
    let data = {"digitalSignature":sig, "email":email};
    postDataResponse = await axios.get(url,{params:data}).catch((e)=>{postDataResponse = e})
    return postDataResponse
}

export async function retrievePostImages(sessionData){

    let posts = [];
    let error = false;

    try{
        let response = await axios.get("http://localhost:8082/api/users/GetlistOfPosts").catch((e)=>{error=true;})
        posts = response.data.reverse();
    
        if (error === false){

            for (let i=0; i< posts.length; i++){
                let response = await getpost(posts[i].digitalSignature,sessionData.email);
                let data = AESDecryption(response.data.encryptedImg, response.data.iv, Buffer.from(sessionData.sessionId,'hex'));
                
                let imageText = data.split("------")[0];
                let imageData = data.split("------")[1];

                posts[i].imageText = imageText;
                posts[i].imageData = imageData;
        
            }
            console.log(posts);
            return posts;
        }

        return [];
    }
    catch(e){
        return "error";
    }

}