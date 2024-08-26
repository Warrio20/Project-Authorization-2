import { FC } from "react";
import BlackContainer from "./BlackContainer";
import { AppProps } from "../../interfaces/components/AppProps";

const Title:FC<AppProps> = ({ title, subtitle }: AppProps) => {
    return (
        <BlackContainer width="40%" height="auto">
          <div id="title">{title}</div>
          <div id="subtitle">{subtitle}</div>
        </BlackContainer>
    )
}
export default Title;