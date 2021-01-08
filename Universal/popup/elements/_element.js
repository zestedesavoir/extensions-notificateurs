export class ZdsElement extends HTMLElement {
	get style() {
		return ``;
	}
	get template() {
		return ``;
	}

	constructor() {
        super();
	}

	connectedCallback() {
		this.render()
	}
	
	render() {
		this.innerHTML = this.template;

		const style = document.createElement('style')
        style.textContent = this.styles
        this.appendChild(style)
	}
}
