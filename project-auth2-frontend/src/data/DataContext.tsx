import { createContext, FC, ReactNode, useState } from "react";
import { IUser } from "../interfaces/IUser";
import AuthService from "../services/AuthService";
interface IDataContext {
    user: IUser,
    isAuthorized: boolean,
    register: (email: string, username: string, password: string) => void,
    login: (username: string, password: string) => void,
    logout: () => void
}
const DataContext = createContext<IDataContext | undefined>(undefined);
const DataProvider: FC<{ children: ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<IUser>({} as IUser);
    const [isAuthorized, setAuth] = useState<boolean>(false);
    const login = async (username: string, password: string) => {
        try {
            const res = await AuthService.login(username, password);
            localStorage.setItem("token", res.data.accessToken);
            setAuth(true);
            setUser(res.data.user);
            console.log(user);
        } catch (e: any) {
            console.log(e.response?.data?.message);
        }
    }
    const register = async (username: string, email: string, password: string) => {
        try {
            const res = await AuthService.register(username, password, email);
            localStorage.setItem("token", res.data.accessToken);
            setAuth(true);
            setUser(res.data.user);
        } catch (e: any) {
            console.log(e.response?.data?.message);
        }
    }
    const logout = async () => {
        await AuthService.logout();
        localStorage.removeItem("token");
        setAuth(false);
        setUser({} as IUser);
    }
    return (
    <DataContext.Provider value={{user, isAuthorized, login, logout, register}}>
        {children}
    </DataContext.Provider>)
}
export {DataProvider, DataContext}