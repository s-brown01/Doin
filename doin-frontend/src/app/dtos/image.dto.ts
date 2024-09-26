export class ImageDTO{
    id: number;
    name: string;
    data: string;

    constructor(
        id: number,
        name: string,
        data: string
    ){
        this.id = id;
        this.name = name;
        this.data = data;
    }
}