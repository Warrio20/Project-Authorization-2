import { FC } from "react";
import "../styles/loading.css"
import loadingGif from "../assets/loading.gif"
const LoadingScreen:FC = () => {
    return (
        <div className="loading">Loading... <img src={loadingGif} alt="" /></div>
    )
}
export default LoadingScreen;