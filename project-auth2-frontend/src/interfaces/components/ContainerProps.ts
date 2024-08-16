import { ReactElement } from "react";

export interface ContainerProps {
    width?: string | number | undefined
    height?: string | number | undefined
    children: ReactElement[] | ReactElement;
};