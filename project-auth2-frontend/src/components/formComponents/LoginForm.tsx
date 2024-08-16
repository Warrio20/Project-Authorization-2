import { FC, useContext, useState } from "react";
import "../../styles/form.css"
import { DataContext } from "../../data/DataContext";
const LoginForm:FC = () => {
    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const context = useContext(DataContext);
    return (<form>
        <label>Sign-in</label>
        <input onChange={e => setUsername(e.target.value)} value={username} placeholder="Username" type="text"></input>
        <input onChange={e => setPassword(e.target.value)} value={password} placeholder="Password" type="password"></input>
        <label><a>Forgot your password?</a></label>
        <button onClick={() => context?.login(username,password)}>Submit</button>
    </form>)
}
export default LoginForm;