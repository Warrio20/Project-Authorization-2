import { FC, useContext, useState } from "react";
import "../../styles/form.css"
import { DataContext } from "../../data/DataContext";
import { AxiosError } from "axios";
const RegisterForm:FC = () => {
    const [username, setUsername] = useState<string>('')
    const [email, setEmail] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [isErr, setIsError] = useState(false);
    const [res, setRes] = useState("");
    const context = useContext(DataContext);
    return (
    <form>
        <label>Registration</label>
        <input placeholder="Username" type="text" onChange={e => setUsername(e.target.value)} value={username}></input>
        <input placeholder="Email" type="email" onChange={e => setEmail(e.target.value)} value={email}></input>
        <input placeholder="Password" type="password" onChange={e => setPassword(e.target.value)} value={password}></input>
        <div style={{color: isErr ? "red" : "green"}}>{res}</div>
        <button type="button" onClick={async () => {
            try {
                await context?.register(username,email,password);
                setIsError(false);
                setRes("You have been succesfully registered, check your email to activate your account");
            } catch(e: any) {
                if(e instanceof AxiosError){
                    setIsError(true);
                    setRes(e.response?.data?.message)
                }
            }
            setPassword(""); setEmail(""); setUsername("")
            }}>Submit</button>
            <div className="loadingDiv" style={{display: context?.isProcessing ? undefined : "none" }}></div>
    </form>)
}
export default RegisterForm;