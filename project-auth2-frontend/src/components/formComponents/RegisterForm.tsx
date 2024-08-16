import { FC, useContext, useState } from "react";
import "../../styles/form.css"
import { DataContext } from "../../data/DataContext";
const RegisterForm:FC = () => {
    const [username, setUsername] = useState<string>('')
    const [email, setEmail] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const context = useContext(DataContext);
    return (
    <form>
        <label>Registration</label>
        <input placeholder="Username" type="text" onChange={e => setUsername(e.target.value)} value={username}></input>
        <input placeholder="Email" type="email" onChange={e => setEmail(e.target.value)} value={email}></input>
        <input placeholder="Password" type="password" onChange={e => setPassword(e.target.value)} value={password}></input>
        <button type="button" onClick={() => context?.register(username,password,email)}>Submit</button>
    </form>)
}
export default RegisterForm;