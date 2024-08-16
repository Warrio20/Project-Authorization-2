import { FC } from "react";
import "../../styles/container.css"
import { ContainerProps } from "../../interfaces/components/ContainerProps";

const BlackContainer:FC<ContainerProps> = ({width, height, children}: ContainerProps) => {
    return (
    <div className="container">
        <div className="sub-container" style={{width: width, height: height}}>
            {children}
        </div>
      </div>
    )
}
export default BlackContainer;