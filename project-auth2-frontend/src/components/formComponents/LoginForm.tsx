import { FC, useContext, useState } from "react";
import "../../styles/form.css"
import { DataContext } from "../../data/DataContext";
import { Link } from "react-router-dom";
import { AxiosError } from "axios";
const LoginForm: FC = () => {
    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [isErr, setIsError] = useState(false);
    const [res, setRes] = useState("");
    const context = useContext(DataContext);
    return (
            <form>
                <label>Sign-in</label>
                <input onChange={e => setUsername(e.target.value)} value={username} placeholder="Username" type="text"></input>
                <input onChange={e => setPassword(e.target.value)} value={password} placeholder="Password" type="password"></input>
                <div className=""></div>
                <label><Link to="/forgot-password">Forgot your password?</Link></label>
                <div style={{ color: isErr ? "red" : "green" }}>{res}</div>
                <button type="button" onClick={async () => {
                    try {
                        const res = await context?.login(username, password);
                        console.log(res)
                        setIsError(false);
                        setRes("You successfully logged into account")
                        setPassword(""); setUsername("");
                    } catch (e: any) {
                        if (e instanceof AxiosError) {
                            setIsError(true);
                            setRes(e.response?.data?.message);
                        }
                    }
                }}> Submit </button>
                <div className="loadingDiv" style={{display: context?.isProcessing ? undefined : "none" }}></div>
            </form>
    )
}
export default LoginForm;